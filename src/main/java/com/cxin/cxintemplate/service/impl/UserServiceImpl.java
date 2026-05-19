package com.cxin.cxintemplate.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxin.cxintemplate.infrastructure.convert.UserConvert;
import com.cxin.cxintemplate.infrastructure.enums.UserRoleEnum;
import com.cxin.cxintemplate.infrastructure.exception.BusinessException;
import com.cxin.cxintemplate.infrastructure.exception.ErrorCode;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserLoginRequest;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserQueryRequest;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserRegisterRequest;
import com.cxin.cxintemplate.infrastructure.model.entity.User;
import com.cxin.cxintemplate.infrastructure.model.vo.user.LoginUserVO;
import com.cxin.cxintemplate.infrastructure.model.vo.user.UserVO;
import com.cxin.cxintemplate.infrastructure.utils.EncryptUtils;
import com.cxin.cxintemplate.mapper.UserMapper;
import com.cxin.cxintemplate.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import static com.cxin.cxintemplate.infrastructure.constant.UserConstant.USER_LOGIN_STATE;


/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author Charles Chen
 * @since 2026-03-21
 */
@Service

public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserConvert userConvert;

    @Override
    public String userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 校验 (基础长度和非空由DTO注解保证，此处仅校验密码一致性)
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 检查是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 3. 加密
        String encryptPassword = EncryptUtils.getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 加密
        String encryptPassword = EncryptUtils.getEncryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 4. 获得脱敏后的用户信息
        return userConvert.userToLoginUserVO(user);

    }

    @Override
    public LoginUserVO getLoginUserVO(HttpServletRequest request) {
        // 先判断是否已登录
        User currentUser = getCurrentUser(request);
        return userConvert.userToLoginUserVO(currentUser);
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        String userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }


    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq((!StrUtil.isEmpty(id)), "id", id);
        queryWrapper.eq((!StrUtil.isEmpty(userRole)), "userRole", userRole);
        queryWrapper.like((!StrUtil.isEmpty(userAccount)), "userAccount", userAccount);
        queryWrapper.like((!StrUtil.isEmpty(userName)), "userName", userName);
        queryWrapper.like((!StrUtil.isEmpty(userProfile)), "userProfile", userProfile);
        queryWrapper.orderBy((!StrUtil.isEmpty(sortField) && !StrUtil.isEmpty(sortOrder)), "ascend".equals(sortOrder), sortField);
        return queryWrapper;
    }


    @Override
    public UserVO getUserVOById(String id) {
        // 先判断是否已登录
        User user = this.getById(id);
        return userConvert.userToUserVO(user);
    }

    @Override
    public Page<UserVO> listUserVOByPage(UserQueryRequest userQueryRequest) {
        long pageNum = userQueryRequest.getPageNum();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = this.page(Page.of(pageNum, pageSize),
                getQueryWrapper(userQueryRequest));
        return userConvert.userPageToUserVoPage(userPage);
    }


}

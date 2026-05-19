package com.cxin.cxintemplate.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserLoginRequest;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserQueryRequest;
import com.cxin.cxintemplate.infrastructure.model.dto.user.UserRegisterRequest;
import com.cxin.cxintemplate.infrastructure.model.entity.User;
import com.cxin.cxintemplate.infrastructure.model.vo.user.LoginUserVO;
import com.cxin.cxintemplate.infrastructure.model.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author Charles Chen
 * @since 2026-03-21
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 新用户 id
     */
    String userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @return 登录用户
     */
    LoginUserVO userLogin(@Valid UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request 请求
     * @return 登录用户
     */
    LoginUserVO getLoginUserVO(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return 是否注销成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request 登录请求
     * @return 登录用户
     */
    User getCurrentUser(HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 查询条件
     * @return 查询条件
     */
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    UserVO getUserVOById(String id);

    /**
     * 获取用户列表
     *
     * @param userQueryRequest 用户查询条件
     * @return 用户列表
     */
    Page<UserVO> listUserVOByPage(@NotBlank(message = "请求体不能为空") UserQueryRequest userQueryRequest);
}

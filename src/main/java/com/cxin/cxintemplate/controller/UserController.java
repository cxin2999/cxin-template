package com.cxin.cxintemplate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cxin.cxintemplate.infrastructure.annotation.AuthCheck;
import com.cxin.cxintemplate.infrastructure.common.BaseResponse;
import com.cxin.cxintemplate.infrastructure.common.DeleteRequest;
import com.cxin.cxintemplate.infrastructure.common.ResultUtils;
import com.cxin.cxintemplate.infrastructure.constant.UserConstant;
import com.cxin.cxintemplate.infrastructure.convert.UserConvert;
import com.cxin.cxintemplate.infrastructure.exception.ErrorCode;
import com.cxin.cxintemplate.infrastructure.exception.ThrowUtils;
import com.cxin.cxintemplate.infrastructure.model.dto.user.*;
import com.cxin.cxintemplate.infrastructure.model.entity.User;
import com.cxin.cxintemplate.infrastructure.model.vo.user.LoginUserVO;
import com.cxin.cxintemplate.infrastructure.model.vo.user.UserVO;
import com.cxin.cxintemplate.infrastructure.utils.EncryptUtils;
import com.cxin.cxintemplate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private UserConvert userConvert;

    @PostMapping("/register")
    @Operation(operationId = "userRegister", summary = "用户注册")
    public BaseResponse<String> userRegister(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    @PostMapping("/login")
    @Operation(operationId = "userLogin", summary = "用户登录")
    public BaseResponse<LoginUserVO> userLogin(@Valid @RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        return ResultUtils.success(userService.userLogin(userLoginRequest, httpServletRequest));
    }

    @GetMapping("/get/login")
    @Operation(operationId = "getLoginUser", summary = "获取登录用户")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getLoginUserVO(request));
    }

    @GetMapping("/logout")
    @Operation(operationId = "userLogout", summary = "用户登出")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }


    /**
     * 创建用户
     */
    @PostMapping("/add")
    @Operation(operationId = "addUser", summary = "创建用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = EncryptUtils.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @Operation(operationId = "getUser", summary = "根据 id 获取用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@RequestParam @NotBlank(message = "ID不能为空") String id) {
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    @Operation(operationId = "getUserVO", summary = "根据 id 获取包装类")
    public BaseResponse<UserVO> getUserVOById(@RequestParam @NotBlank(message = "ID不能为空") String id) {
        UserVO userVO = userService.getUserVOById(id);
        ThrowUtils.throwIf(userVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userVO);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @Operation(operationId = "deleteUser", summary = "删除用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody @NotNull(message = "请求体不能为空") @Valid DeleteRequest deleteRequest) {
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @Operation(operationId = "updateUser", summary = "更新用户")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody @NotNull(message = "请求体不能为空") @Valid UserUpdateRequest userUpdateRequest) {
        User user = userConvert.userUpdateRequestToUser(userUpdateRequest);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @Operation(operationId = "listUserVOByPage", summary = "分页获取用户封装列表")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody @NotNull(message = "请求体不能为空") UserQueryRequest userQueryRequest) {
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return ResultUtils.success(userVOPage);
    }


}

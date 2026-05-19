package com.cxin.cxintemplate.infrastructure.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, message = "用户账号过短")
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "用户密码过短")
    private String userPassword;

    /**
     * 确认密码
     */
    @Size(min = 8, message = "确认密码过短")
    @NotBlank(message = "确认密码不能为空")
    private String checkPassword;
}

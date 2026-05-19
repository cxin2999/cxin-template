package com.cxin.cxintemplate.infrastructure.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtils {
    //todo init
    public static final String PASSWORD_SALT = "bafenbao";

    public static String getEncryptPassword(String userPassword) {
        return DigestUtils.md5DigestAsHex((userPassword + PASSWORD_SALT).getBytes());
    }
}

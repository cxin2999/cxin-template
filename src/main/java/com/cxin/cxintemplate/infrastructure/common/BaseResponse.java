package com.cxin.cxintemplate.infrastructure.common;

import com.cxin.cxintemplate.infrastructure.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author charleschen
 * @Date 2026/3/3 21:41
 * @Description
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}


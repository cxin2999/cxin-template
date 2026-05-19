package com.cxin.cxintemplate.infrastructure.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author charleschen
 * @Date 2026/3/3 21:51
 * @Description
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    @NotBlank(message = "id不能为空")
    private String id;

    private static final long serialVersionUID = 1L;
}


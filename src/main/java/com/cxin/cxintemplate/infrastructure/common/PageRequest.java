package com.cxin.cxintemplate.infrastructure.common;

import lombok.Data;

/**
 * @Author charleschen
 * @Date 2026/3/3 21:50
 * @Description
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}


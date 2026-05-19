package com.cxin.cxintemplate.infrastructure.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ParseJsonUtils {
    /**
     * 解析 JSON 响应
     */
    public static <T> T parseJsonResponse(String content, Class<T> clazz, String name) {
        try {
            return JSONUtil.toBean(content, clazz);
        } catch (Exception e) {
            log.error("{}解析失败, content={}", name, content, e);
            throw new RuntimeException(name + "解析失败");
        }
    }

    /**
     * 解析 JSON 响应（数组）
     */
    public static <T> List<T> parseJsonArrayResponse(String content, Class<T> clazz, String name) {
        try {
            return JSONUtil.toList(content, clazz);
        } catch (Exception e) {
            log.error("{}解析失败, content={}", name, content, e);
            throw new RuntimeException(name + "解析失败");
        }
    }
}

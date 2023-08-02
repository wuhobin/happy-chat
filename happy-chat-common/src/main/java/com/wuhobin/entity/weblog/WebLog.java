package com.wuhobin.entity.weblog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/7/10 16:12
 * @description
 */
@Data
@Builder
@AllArgsConstructor
public class WebLog implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回数据
     */
    private Object result;
}

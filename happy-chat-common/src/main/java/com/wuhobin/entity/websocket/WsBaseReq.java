package com.wuhobin.entity.websocket;

import lombok.Data;

/**
 * Description: websocket前端请求体
 * Date: 2023-03-19
 * @author wuhongbin
 */
@Data
public class WsBaseReq {
    /**
     * 请求类型 1.请求登录二维码，2心跳检测
     *
     * @see
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}

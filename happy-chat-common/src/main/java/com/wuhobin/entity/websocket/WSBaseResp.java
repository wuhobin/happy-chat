package com.wuhobin.entity.websocket;

import lombok.Data;

/**
 * Description: ws的基本返回信息体
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 * @author admin
 */
@Data
public class WSBaseResp<T> {
    /**
     * ws推送给前端的消息
     *
     */
    private Integer type;

    private T data;
}

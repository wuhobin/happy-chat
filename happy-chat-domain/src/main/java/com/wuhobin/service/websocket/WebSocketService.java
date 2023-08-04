package com.wuhobin.service.websocket;

import io.netty.channel.Channel;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/4 14:15
 * @description
 */
public interface WebSocketService {

    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);


    /**
     * 处理ws断开连接的事件
     *
     * @param channel
     */
    void removed(Channel channel);

}

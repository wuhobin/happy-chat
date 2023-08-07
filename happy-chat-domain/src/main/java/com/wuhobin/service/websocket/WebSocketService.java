package com.wuhobin.service.websocket;

import com.wuhobin.entity.websocket.WSBaseResp;
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

    /**
     * 推动消息给所有在线的人
     *
     * @param wsBaseResp 发送的消息体
     * @param skipUid    需要跳过的人
     */
    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);

}

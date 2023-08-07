package com.wuhobin.service.websocket.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.entity.websocket.WSBaseResp;
import com.wuhobin.service.websocket.WebSocketService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/4 14:15
 * @description
 */
@Slf4j
@Component
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private UserOnlineCache userOnlineCache;

    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, Long> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();


    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, 1000L);
    }

    @Override
    public void removed(Channel channel) {
        
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid) {
        ONLINE_WS_MAP.forEach((channel, userId) -> {
//            if (ObjectUtil.equal(userId, skipUid)) {
//                return;
//            }
            sendMsg(channel, wsBaseResp);
        });
    }

    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }


}

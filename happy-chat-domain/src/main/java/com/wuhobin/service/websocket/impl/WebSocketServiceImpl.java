package com.wuhobin.service.websocket.impl;

import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.service.websocket.WebSocketService;
import io.netty.channel.Channel;
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

    }

    @Override
    public void removed(Channel channel) {

    }
}

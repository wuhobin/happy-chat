package com.wuhobin.service.websocket.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.config.security.TokenCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.entity.websocket.WsBaseReq;
import com.wuhobin.entity.websocket.WsBaseResp;
import com.wuhobin.entity.websocket.WsChannelExtraDTO;
import com.wuhobin.event.UserOfflineEvent;
import com.wuhobin.event.UserOnlineEvent;
import com.wuhobin.service.adapter.WsAdapter;
import com.wuhobin.service.websocket.WebSocketService;
import com.wuhobin.utils.JwtUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.swagger.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
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

    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 所有已连接的websocket连接列表和一些额外参数
     */
    private static final ConcurrentHashMap<Channel, WsChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    /**
     * 所有在线的用户和对应的socket
     */
    private static final ConcurrentHashMap<Long, CopyOnWriteArrayList<Channel>> ONLINE_UID_MAP = new ConcurrentHashMap<>();


    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WsChannelExtraDTO());
    }

    @Override
    public void removed(Channel channel) {
        WsChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        Optional<Long> uidOptional = Optional.ofNullable(wsChannelExtraDTO)
                .map(WsChannelExtraDTO::getUserId);
        boolean offlineAll = offline(channel, uidOptional);
        log.info("【webSocketService】 用户全部下线成功");
        if (uidOptional.isPresent() && offlineAll) {
            UserInfoDO user = new UserInfoDO();
            user.setId(uidOptional.get());
            user.setLoginDate(new Date());
            applicationEventPublisher.publishEvent(new UserOfflineEvent(this, user));
        }
    }

    @Override
    public void authorize(Channel channel, WsBaseReq wsBaseReq) {
        JSONObject jsonObject = JSONUtil.parseObj(wsBaseReq.getData());
        String token = jsonObject.getStr("token");
        Long userId = JwtUtils.getUserIdByToken(token);
        String cacheToken = tokenCache.getToken(userId);
        if (StringUtils.isNotBlank(token) && token.equals(cacheToken)) {
            jsonObject.set("userId", userId);
            WsBaseReq req = new WsBaseReq();
            req.setData(JSONUtil.toJsonStr(jsonObject));
            loginSuccess(channel,req);
        }else {
            sendMsg(channel, WsAdapter.buildInvalidateTokenResp());
        }
    }



    @Override
    public void loginSuccess(Channel channel, WsBaseReq wsBaseReq) {
        JSONObject jsonObject = JSONUtil.parseObj(wsBaseReq.getData());
        Long userId = jsonObject.getLong("userId");
        updateOnlineList(channel, userId);
        Boolean online = userOnlineCache.isOnline(userId);
        if (!online){
            userOnlineCache.addOnlineUser(userId);
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            UserInfoDO userInfoDO = new UserInfoDO();
            userInfoDO.setId(userId);
            userInfoDO.setLoginDate(new Date());
            userInfoDO.setLoginIp(address.getAddress().getHostAddress());
            applicationEventPublisher.publishEvent(new UserOnlineEvent(this, userInfoDO));
        }
    }

    /**
     * 用户下线
     * return 是否全下线成功
     */
    private boolean offline(Channel channel, Optional<Long> uidOptional) {
        ONLINE_WS_MAP.remove(channel);
        if (uidOptional.isPresent()) {
            CopyOnWriteArrayList<Channel> channels = ONLINE_UID_MAP.get(uidOptional.get());
            if (CollectionUtil.isNotEmpty(channels)) {
                channels.removeIf(ch -> Objects.equals(ch, channel));
            }
            return CollectionUtil.isEmpty(ONLINE_UID_MAP.get(uidOptional.get()));
        }
        return true;
    }

    /**
     * 用户上线 更新上线列表
     * @param channel
     * @param userId
     */
    private void updateOnlineList(Channel channel, Long userId) {
        getOrInitChannelExt(channel).setUserId(userId);
        ONLINE_UID_MAP.putIfAbsent(userId, new CopyOnWriteArrayList<>());
        ONLINE_UID_MAP.get(userId).add(channel);
    }


    /**
     * 如果在线列表不存在，就先把该channel放进在线列表
     *
     * @param channel
     * @return
     */
    private WsChannelExtraDTO getOrInitChannelExt(Channel channel) {
        WsChannelExtraDTO wsChannelExtraDTO =
                ONLINE_WS_MAP.getOrDefault(channel, new WsChannelExtraDTO());
        WsChannelExtraDTO old = ONLINE_WS_MAP.putIfAbsent(channel, wsChannelExtraDTO);
        return ObjectUtil.isNull(old) ? wsChannelExtraDTO : old;
    }



    private void sendMsg(Channel channel, WsBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }

    @Override
    public void sendToAllOnline(WsBaseResp<?> wsBaseResp, Long skipUid) {
        ONLINE_WS_MAP.forEach((channel, userId) -> {
            if (ObjectUtil.equal(userId, skipUid)) {
                return;
            }
            sendMsg(channel, wsBaseResp);
        });
    }

    @Override
    public void sendToAllOnline(WsBaseResp<?> wsBaseResp) {
        sendToAllOnline(wsBaseResp,null);
    }


}

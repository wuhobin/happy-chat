package com.wuhobin.service.adapter;

import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.entity.websocket.WsBaseResp;
import com.wuhobin.entity.websocket.WsOnlineOfflineNotify;
import com.wuhobin.enums.WsRespTypeEnum;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.vo.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 16:28
 * @description
 */
@Component
@Slf4j
public class WsAdapter {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserOnlineCache userOnlineCache;

    /**
     * 构建消息 让前端的token失效
     * @return
     */
    public static WsBaseResp<?> buildInvalidateTokenResp() {
        WsBaseResp<String> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }

    public WsBaseResp<?> buildOnlineNotifyResp(UserInfoDO user) {
        WsBaseResp<WsOnlineOfflineNotify<UserInfoDto>> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsRespTypeEnum.ONLINE_OFFLINE_NOTIFY.getType());
        WsOnlineOfflineNotify<UserInfoDto> onlineOfflineNotify = new WsOnlineOfflineNotify<UserInfoDto>();
        onlineOfflineNotify.setChangeList(userInfoService.selectOnlineUserList(Collections.singleton(user.getId())));
        onlineOfflineNotify.setOnlineNum((long) userOnlineCache.getOnlineUserCount());
        wsBaseResp.setData(onlineOfflineNotify);
        return wsBaseResp;
    }
}

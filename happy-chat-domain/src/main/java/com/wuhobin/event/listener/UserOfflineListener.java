package com.wuhobin.event.listener;


import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.event.UserOfflineEvent;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.service.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

/**
 * 用户下线监听器
 *
 * @author wuhognbin create on 2022/08/26
 */
@Slf4j
@Component
public class UserOfflineListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserOnlineCache userOnlineCache;


    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveRedisAndPush(UserOfflineEvent event) {
        UserInfoDO user = event.getUser();
        userOnlineCache.removeOnlineUser(user.getId());
    }

    @Async
    @EventListener(classes = UserOfflineEvent.class)
    public void saveDB(UserOfflineEvent event) {
        UserInfoDO user = event.getUser();
        UserInfoDO update = new UserInfoDO();
        update.setId(user.getId());
        update.setLoginDate(user.getLoginDate());
        userInfoService.updateById(update);
    }

}

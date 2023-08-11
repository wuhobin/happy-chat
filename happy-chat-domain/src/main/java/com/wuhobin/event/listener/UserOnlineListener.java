package com.wuhobin.event.listener;


import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.event.UserOnlineEvent;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.service.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

/**
 * 用户上线监听器
 *
 * @author wuhongbin create on 2023/08/26
 */
@Slf4j
@Component
public class UserOnlineListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserOnlineCache userOnlineCache;


    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveRedisAndPush(UserOnlineEvent event) {
        UserInfoDO user = event.getUser();
        userOnlineCache.addOnlineUser(user.getId());
    }

    @Async
    @EventListener(classes = UserOnlineEvent.class)
    public void saveDB(UserOnlineEvent event) {
        UserInfoDO user = event.getUser();
        UserInfoDO update = new UserInfoDO();
        update.setId(user.getId());
        update.setLoginDate(user.getLoginDate());
        userInfoService.updateById(update);

    }

}

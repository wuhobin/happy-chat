package com.wuhobin.event.listener;


import com.wuhobin.cache.UserOnlineCache;
import com.wuhobin.dataobject.UserInfoDO;
import com.wuhobin.entity.ipaddress.AddressResult;
import com.wuhobin.event.UserOnlineEvent;
import com.wuhobin.rest.ipaddress.IpAddressService;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.service.websocket.WebSocketService;
import com.wuhobin.utils.http.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private IpAddressService ipAddressService;


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
        AddressResult ipAddress = ipAddressService.getIpAddress(user.getLoginIp());
        update.setId(user.getId());
        update.setLoginDate(user.getLoginDate());
        update.setLoginIp(user.getLoginIp());
        update.setCity(ObjectUtils.isEmpty(ipAddress) ? "未知" : ipAddress.getContent().getAddress());
        userInfoService.updateById(update);

    }

}

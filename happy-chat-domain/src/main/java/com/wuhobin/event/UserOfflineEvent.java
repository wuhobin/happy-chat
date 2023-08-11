package com.wuhobin.event;


import com.wuhobin.dataobject.UserInfoDO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author wuhongbin
 */
@Getter
public class UserOfflineEvent extends ApplicationEvent {
    private final UserInfoDO user;

    public UserOfflineEvent(Object source, UserInfoDO user) {
        super(source);
        this.user = user;
    }
}

package com.wuhobin.enums;

import com.wuhobin.entity.websocket.WsOnlineOfflineNotify;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 * @author wuhongbin
 */
@AllArgsConstructor
@Getter
public enum WsRespTypeEnum {

    ONLINE_OFFLINE_NOTIFY(5, "用户上线", WsOnlineOfflineNotify.class),
    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),

    ;

    private final Integer type;
    private final String desc;
    private final Class dataClass;

    private static Map<Integer, WsRespTypeEnum> cache;

    static {
        cache = Arrays.stream(WsRespTypeEnum.values()).collect(Collectors.toMap(WsRespTypeEnum::getType, Function.identity()));
    }

    public static WsRespTypeEnum of(Integer type) {
        return cache.get(type);
    }
}

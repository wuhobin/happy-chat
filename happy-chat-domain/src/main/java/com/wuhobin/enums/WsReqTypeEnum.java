package com.wuhobin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: ws前端请求类型枚举
 * Date: 2023-03-19
 * @author admin
 */
@AllArgsConstructor
@Getter
public enum WsReqTypeEnum {
    LOGIN(1, "登录成功"),
    HEARTBEAT(2, "心跳包"),

    AUTHORIZE(3, "登录认证"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WsReqTypeEnum> cache;

    static {
        cache = Arrays.stream(WsReqTypeEnum.values()).collect(Collectors.toMap(WsReqTypeEnum::getType, Function.identity()));
    }

    public static WsReqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}

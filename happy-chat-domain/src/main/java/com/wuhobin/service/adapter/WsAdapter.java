package com.wuhobin.service.adapter;

import com.wuhobin.entity.websocket.WsBaseResp;
import com.wuhobin.enums.WsRespTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/11 16:28
 * @description
 */
@Component
@Slf4j
public class WsAdapter {

    /**
     * 构建消息 让前端的token失效
     * @return
     */
    public static WsBaseResp<?> buildInvalidateTokenResp() {
        WsBaseResp<String> wsBaseResp = new WsBaseResp<>();
        wsBaseResp.setType(WsRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }
}

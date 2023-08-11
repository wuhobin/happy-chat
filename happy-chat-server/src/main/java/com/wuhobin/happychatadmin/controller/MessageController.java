package com.wuhobin.happychatadmin.controller;

import com.wuhobin.api.CommonResult;
import com.wuhobin.entity.websocket.WsBaseResp;
import com.wuhobin.service.websocket.WebSocketService;
import com.wuhobin.utils.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/7 15:53
 * @description
 */
@Api(value = "用户消息接口",tags = {"用户消息接口"})
@RestController
@RequestMapping("/api/msg")
@Slf4j
public class MessageController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/send")
    public CommonResult sendMessage(){
        WsBaseResp<String> resp = new WsBaseResp<>();
        resp.setType(1);
        resp.setData("服务端主动发送消息");
        webSocketService.sendToAllOnline(resp, SecurityUtils.getUserId());
        return CommonResult.success();
    }


}

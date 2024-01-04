package com.wuhobin.config.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.wuhobin.entity.websocket.WsBaseReq;
import com.wuhobin.enums.WsReqTypeEnum;
import com.wuhobin.service.websocket.WebSocketService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/4 10:34
 * @description
 */
@Slf4j
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;

    /**
     * 当web客户端连接后，触发该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("当前有客户端连接 ctx={}", ctx);
        this.webSocketService = getService();
    }

    /**
     * 客户端离线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("当前有客户端离线 ctx={}", ctx);
        userOffLine(ctx);
    }


    /**
     * 可能出现业务判断离线后再次触发 channelInactive
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("当前有客户端离线 ctx={}", ctx);
        userOffLine(ctx);
    }

    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("心跳检查 进入IdleStateEvent");
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("心跳检查 进入HandshakeComplete evt={}", evt);
            webSocketService.connect(ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 当客户端发送消息时
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WsBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WsBaseReq.class);
        WsReqTypeEnum wsReqTypeEnum = WsReqTypeEnum.of(wsBaseReq.getType());
        switch (wsReqTypeEnum) {
            case LOGIN:
                log.info("【websocket收到前端消息】 用户登录成功 wsBaseReq={}", wsBaseReq);
                webSocketService.loginSuccess(ctx.channel(), wsBaseReq);
                break;
            case HEARTBEAT:
                break;
            case AUTHORIZE:
                log.info("【websocket收到前端消息】 用户主动认证 wsBaseReq={}", wsBaseReq);
                webSocketService.authorize(ctx.channel(), wsBaseReq);

                break;
            default:
                log.info("未知类型");
        }
    }

    private void userOffLine(ChannelHandlerContext ctx) {
        this.webSocketService.removed(ctx.channel());
        ctx.channel().close();
    }

    private WebSocketService getService() {
        return SpringUtil.getBean(WebSocketService.class);
    }
}

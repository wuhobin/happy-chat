package com.wuhobin.config.security;

import cn.hutool.json.JSONUtil;
import com.wuhobin.api.CommonResult;
import com.wuhobin.api.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wuhongbin
 * @description: 没有权限访问 自定义逻辑处理
 * @datetime 2023/01/13 10:06
 */
@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSONUtil.parseObj(CommonResult.failed(ResultCode.FORBIDDEN)).toString());
    }
}

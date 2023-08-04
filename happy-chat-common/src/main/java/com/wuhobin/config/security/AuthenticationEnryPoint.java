package com.wuhobin.config.security;

import cn.hutool.json.JSONUtil;
import com.wuhobin.api.CommonResult;
import com.wuhobin.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wuhongbin
 * @description: spring security 未登录处理逻辑
 * @datetime 2023/01/12 16:21
 */
@Component
@Slf4j
public class AuthenticationEnryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String msg = null;
        if (e instanceof BadCredentialsException){
            msg = ResultCode.VERIFY_CODE_ERROR.getMessage();
        } else if (e instanceof InternalAuthenticationServiceException){
            msg = e.getMessage();
        } else if (e instanceof InsufficientAuthenticationException){
            msg = ResultCode.MSG_NOT_LOGIN.getMessage();
        }else {
            msg = "未登录，无法访问相应资源！";
        }
        httpServletResponse.getWriter().write(JSONUtil.parseObj(CommonResult.failed(-1000,msg)).toString());
    }
}


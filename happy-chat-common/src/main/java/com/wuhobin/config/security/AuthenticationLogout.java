package com.wuhobin.config.security;

import cn.hutool.json.JSONUtil;
import com.wuhobin.api.CommonResult;
import com.wuhobin.api.ResultCode;
import com.wuhobin.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wuhongbin
 * @description: 自定义退出登录逻辑
 * @datetime 2023/01/12 17:05
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {

    @Autowired
    private TokenCache tokenCache;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isNotBlank(token)){
            Long userId = JwtUtils.getUserIdByToken(token);
            tokenCache.deleteToken(userId);
        }
        httpServletResponse.getWriter().write(JSONUtil.parseObj(CommonResult.success()).toString());
    }
}

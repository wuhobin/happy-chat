package com.wuhobin.config.security.mobile;

import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wuhongbin
 * @description: 类比于UsernamePasswordAuthenticationFilter
 * @datetime 2023/01/17 15:43
 */
public class MobileCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/mobile/login",
            "POST");

    /**
     * 前端传来的 参数名 - 用于request.getParameter 获取
     */
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;


    /**
     * 是否 仅仅post
     */
    private boolean postOnly = true;

    public MobileCodeAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    /**
     * 通过 传入的 参数 创建 匹配器
     * 即 Filter过滤的url
     */
    public MobileCodeAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String mobile = obtainMobile(request);
        mobile = (mobile != null) ? mobile : "";
        mobile = mobile.trim();
        MobileCodeAuthenticationToken authRequest = new MobileCodeAuthenticationToken(mobile);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    @Nullable
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }


    protected void setDetails(HttpServletRequest request, MobileCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}

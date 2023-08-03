package com.wuhobin.config.security.mobile;

import com.wuhobin.config.security.TokenCache;
import com.wuhobin.service.user.UserDetailService;
import com.wuhobin.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wuhongbin
 * @description: spring security jwt验证
 * @datetime 2023/01/13 12:20
 */
@Component
@Slf4j
public class JwtMobileAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private TokenCache tokenCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        log.info("自定义 mobile token拦截过滤器 token={}", token);
        if (StringUtils.isNotBlank(token)) {
            String nickName = JwtUtils.getNickNameByToken(token);
            String mobile = JwtUtils.getMobileByToken(token);
            String cacheToken = tokenCache.getToken(mobile);
            log.info("自定义 mobile token拦截过滤器 nickName = {},mobile={}", nickName, mobile);
            if (StringUtils.isBlank(cacheToken) || !token.equals(cacheToken)) {
                log.info("自定义 mobile token拦截过滤器 token为空或者不一致，直接放行");
                filterChain.doFilter(request, response);
                return;
            }
            if (StringUtils.isNotBlank(nickName) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByMobile(mobile);
                if (JwtUtils.validateToken(token)) {
                    //给使用该JWT令牌的用户进行授权
                    MobileCodeAuthenticationToken authenticationToken
                            = new MobileCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("自定义 mobile token拦截过滤器 存入spring security 上下文 authenticationToken={},userDetails={}", authenticationToken,userDetails);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

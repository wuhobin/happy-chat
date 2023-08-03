package com.wuhobin.config.security.mobile;

import com.wuhobin.api.ResultCode;
import com.wuhobin.cache.SmsCodeCache;
import com.wuhobin.service.user.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wuhongbin
 * @description: 类比于DaoAuthenticationProvider
 * @datetime 2023/01/17 16:02
 */
@Slf4j
public class MobileCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SmsCodeCache smsCodeCache;


    private UserDetailService userDetailService;

    public MobileCodeAuthenticationProvider(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileCodeAuthenticationToken authenticationToken = (MobileCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        UserDetails user = userDetailService.loadUserByMobile(mobile);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String inputCode = request.getParameter("code");
        log.info("【spring security MobileCodeAuthenticationProvider】  -----> code = {}, password = {}", inputCode, user.getPassword());
        if (!user.getPassword().equals(inputCode)) {
            smsCodeCache.addVerifyCodeErr(mobile);
            log.info("【spring security MobileCodeAuthenticationProvider】  ----->  用户登录-验证码输入错误,mobile={}", mobile);
            throw new BadCredentialsException(ResultCode.VERIFY_CODE_ERROR.getMessage());
        }
        MobileCodeAuthenticationToken token = new MobileCodeAuthenticationToken(user, user.getAuthorities());
        token.setDetails(authenticationToken.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

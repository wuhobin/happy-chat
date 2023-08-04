package com.wuhobin.service.user.impl;

import cn.hutool.system.UserInfo;
import com.wuhobin.api.ResultCode;
import com.wuhobin.cache.SmsCodeCache;
import com.wuhobin.enums.UserStatus;
import com.wuhobin.service.user.UserDetailService;
import com.wuhobin.service.user.UserInfoService;
import com.wuhobin.vo.UserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author wuhongbin
 * @description: UserDetailServiceImpl
 * @datetime 2023/01/12 16:58
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    private UserInfoService userService;

    @Autowired
    private SmsCodeCache smsCodeCache;



    @Override
    public UserDetails loadUserByMobile(String mobile) {
        log.info("【UserDetailServiceImpl 用户手机号验证码登录】 mobile->{}",mobile);
        UserInfoVO userVO = userService.selectUserByMobile(mobile);
        if (ObjectUtils.isEmpty(userVO)){
            userVO = userService.saveUserInfo(mobile);
        }
        if (UserStatus.DISABLE.getCode().equals(userVO.getStatus())){
            log.info("登录用户：{} 已被停用.", mobile);
            throw new InternalAuthenticationServiceException("对不起，您的账号：" + mobile + " 已停用");
        }
        String verifyCode = smsCodeCache.getVerifyCode(mobile);
        if (StringUtils.isBlank(verifyCode)){
            throw new InternalAuthenticationServiceException(ResultCode.VERIFY_CODE_NOT_CLICK.getMessage());
        }
        userVO.setPassword(verifyCode);
        userVO.setUsername(userVO.getNickName());
        return userVO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

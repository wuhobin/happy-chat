package com.wuhobin.happychatadmin.controller;


import com.wuhobin.api.CommonResult;
import com.wuhobin.api.ResultCode;
import com.wuhobin.cache.SmsCodeCache;
import com.wuhobin.config.security.TokenCache;
import com.wuhobin.config.security.mobile.MobileCodeAuthenticationToken;
import com.wuhobin.rest.sms.SmsService;
import com.wuhobin.utils.JwtUtils;
import com.wuhobin.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wuhongbin
 * @description: 登录
 * @datetime 2023/01/11 15:19
 */
@Api(value = "用户接口",tags = {"用户接口"})
@RestController
@RequestMapping("/api/login")
@Slf4j
public class LoginController {

    @Autowired
    private TokenCache tokenCache;

    @Autowired
    private SmsCodeCache smsCodeCache;

    @Autowired
    private SmsService smsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 连续三次验证码错误限制登录
     */
    private static final int RESTRICTION_TIMES = 3;


    /**
     * 发送验证码
     * @return
     */
    @ApiOperation(value = "发送验证码")
    @RequestMapping(value = "send", method = RequestMethod.POST)
    public CommonResult sendCode(String mobile){
        try {
            if (StringUtils.isBlank(mobile)){
                log.error("参数校验失败，mobile为空");
                return CommonResult.failed(ResultCode.VALIDATE_FAILED);
            }
            //判断该用户验证码连续错误次数
            Integer verifyCodeErr = smsCodeCache.getVerifyCodeErr(mobile);
            if (verifyCodeErr >= RESTRICTION_TIMES) {
                log.error("【登录模块】，验证码连续错误三次，两小时内禁止登录!,mobile={}",mobile);
                return CommonResult.failed(ResultCode.ASTRICT_LOGIN);
            }
            //获取限制发送
            String verifyCode = smsCodeCache.getMobileLatter(mobile);
            if (StringUtils.isNotBlank(verifyCode)) {
                log.info("【登录模块】，验证码发送限制时间还未到，不允许发送验证码!,mobile={}", mobile);
                return CommonResult.failed(ResultCode.PLEASE_TRY_LATER);
            }
            String code = smsService.sendCode(mobile);
            if (StringUtils.isBlank(code)){
                log.error("【登录模块】，验证码生成为空，请重新再试!,mobile={}", mobile);
                return CommonResult.failed(ResultCode.SEND_VERIFY_CODE_ERR);
            }
            smsCodeCache.setVerifyCode(mobile,code);
            smsCodeCache.setMobileLatter(mobile);
            return CommonResult.success();
        }catch (Exception e){
            log.error("【登录模块】，发送验证码错误！mobile={}", mobile, e);
            return CommonResult.failed(ResultCode.SEND_VERIFY_CODE_ERR);
        }
    }


    @ApiOperation("手机号验证码登录")
    @PostMapping("/mobile-login")
    public CommonResult mobileLogin(String mobile,String code){
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)){
            log.info("【用户登录】 手机号或者验证码为空");
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }
        //判断该用户验证码连续错误次数
        Integer verifyCodeErr = smsCodeCache.getVerifyCodeErr(mobile);
        if (verifyCodeErr >= RESTRICTION_TIMES) {
            log.error("【用户登录】，验证码连续错误三次，两小时内禁止登录!");
            return CommonResult.failed(ResultCode.ASTRICT_LOGIN);
        }
        MobileCodeAuthenticationToken mobileCodeAuthenticationToken = new MobileCodeAuthenticationToken(mobile);
        Authentication authenticate = authenticationManager.authenticate(mobileCodeAuthenticationToken);

        UserInfoVO userVO = (UserInfoVO) authenticate.getPrincipal();
        log.info("【手机号验证码登录】 登录成功，当前登录用户为: userVO={}",userVO);
        String token = JwtUtils.generateJWT(userVO.getPhoneNum(),userVO.getNickName());
        if (StringUtils.isEmpty(token)) {
            log.error("【用户登录】- 用户登录token为空! phoneNum = {}", userVO.getPhoneNum());
            return CommonResult.failed(ResultCode.LOGIN_FAIL);
        }
        tokenCache.setToken(userVO.getPhoneNum(),token);
        Map<String, Object> map = new HashMap<>(4);
        map.put("token",token);
        map.put("userVo",userVO);
        return CommonResult.success(map);
    }


    @ApiOperation("测试")
    @GetMapping("/test")
    public CommonResult test(){
        return CommonResult.success();
    }


}

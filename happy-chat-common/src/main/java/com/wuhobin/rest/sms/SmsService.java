package com.wuhobin.rest.sms;

/**
 * 发送短信验证码
 * @author wuhongbin
 */
public interface SmsService {


    /**
     * 根据手机号发送验证码
     * @param mobile
     * @return code
     */
    String sendCode(String mobile);


}

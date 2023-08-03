package com.wuhobin.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wuhongbin
 * @description: 手机号验证码缓存
 * @datetime 2023/01/17 11:23
 */
@Component
@Slf4j
public class SmsCodeCache {

    /**
     * 用户验证码key
     */
    private static final String CODE_CACHE_KEY = "happy-chat:sms-code:%s";

    /**
     * 用户验证码失败次数key
     */
    private static final String CODE_ERROR_KEY = "happy-chat:sms-err:%s";

    /**
     * 用户60秒限制key
     */
    private static final String CODE_LAYER_KEY = "happy-chat:sms-latter:%s";


    /**
     * 连续错误三次验证码之后限制登录时间（小时）
     */
    private static final Integer RESTRICTION_DATE = 2;


    @Autowired
    private RedisTemplate redisTemplate;


    public void setVerifyCode(String mobile, String code) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_CACHE_KEY, mobile);
        valueOperations.set(key, code, 300, TimeUnit.SECONDS);
        log.info("存入验证码，mobile={},code={}", mobile, code);
    }

    public String getVerifyCode(String mobile) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_CACHE_KEY, mobile);
        String code = valueOperations.get(key);
        log.info("获取验证码，mobile={},code={}", mobile, code);
        return code;
    }


    /**
     * 增加用户输入验证码错误次数
     *
     * @param mobile
     */
    public void addVerifyCodeErr(String mobile) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_ERROR_KEY, mobile);
        Integer verifyCodeErr = getVerifyCodeErr(mobile);
        valueOperations.set(key, verifyCodeErr + 1, RESTRICTION_DATE, TimeUnit.HOURS);
    }


    /**
     * 获取验证码连续错误次数
     *
     * @param mobile
     * @return
     */
    public Integer getVerifyCodeErr(String mobile) {
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_ERROR_KEY, mobile);
        return Objects.isNull(valueOperations.get(key)) ? 0 : valueOperations.get(key);
    }


    /**
     * 设置用户一分钟后才能再次发送验证码缓存
     *
     * @param mobile
     */
    public void setMobileLatter(String mobile) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_LAYER_KEY, mobile);
        valueOperations.set(key, mobile, 60, TimeUnit.SECONDS);
    }

    public String getMobileLatter(String mobile){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(CODE_LAYER_KEY, mobile);
        return valueOperations.get(key);
    }

}

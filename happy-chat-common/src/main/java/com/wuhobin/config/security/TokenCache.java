package com.wuhobin.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Description: TODO
 * Author: wuhongbin
 * Date: Created in 2023/01/13 16:42
 * Copyright:Copyright (c) 2020
 * Version: 0.0.1
 */
@Slf4j
@Repository
public class TokenCache {

    private static final String TOKEN_CACHE_KEY = "happy-chat:token:%s";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 获取token信息
     *
     * @return
     */
    public String getToken(Long userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(TOKEN_CACHE_KEY,userId);
        String token = valueOperations.get(key);
        log.info("获取用户token userId={},token={}",userId,token);
        if (StringUtils.isBlank(token)) {
            return "";
        }
        return token;
    }

    /**
     * 设置token信息
     * token过期时间为3天
     * @param token
     */
    public void setToken(Long userId, String token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = String.format(TOKEN_CACHE_KEY,userId);
        valueOperations.set(key, token);
        redisTemplate.expire(key, 3, TimeUnit.DAYS);
        log.info("设置用户token userId={},token={}",userId,token);
    }




    public void deleteToken(Long userId){
        String key = String.format(TOKEN_CACHE_KEY,userId);
        redisTemplate.delete(key);
        log.info("删除用户token userId={}",userId);
    }

}

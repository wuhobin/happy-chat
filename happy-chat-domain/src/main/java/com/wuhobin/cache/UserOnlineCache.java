package com.wuhobin.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/4 14:07
 * @description
 */

@Component
@Slf4j
public class UserOnlineCache {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String ONLINE_CHAR = "happy-chat:user:online";


    public boolean addOnlineUser(Long userId){
        SetOperations<String,Long> setOperations = redisTemplate.opsForSet();
        Long add = setOperations.add(ONLINE_CHAR, userId);
        log.info("添加在线用户进set,userId={},add={}", userId, add);
        if (add != null && add > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isOnline(Long userId) {
        SetOperations<String,Long> setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(ONLINE_CHAR, userId);
    }


    public void  removeOnlineUser(Long userId){
        SetOperations<String,Long> setOperations = redisTemplate.opsForSet();
        Long count = setOperations.remove(ONLINE_CHAR, userId);
        log.info("移除在线用户,userId={},count={}", userId, count);
    }

    public int getOnlineUserCount(){
        SetOperations<String,Long> setOperations = redisTemplate.opsForSet();
        Long size = setOperations.size(ONLINE_CHAR);
        if (ObjectUtils.isEmpty(size)){
            return 0;
        }
        return size.intValue();
    }

}

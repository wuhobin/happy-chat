package com.wuhobin.cache;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserNoCache {

    public static final String USER_NO_INDEX = "happy-chat:user-no:index";
    public static final String USER_NO = "happy-chat:user-no:id:";

    @Autowired
    private RedisTemplate redisTemplate;

    public static final int length = 100000;

    public String getNewUserNo(String mobile) {
        Long increment = redisTemplate.opsForValue().increment(USER_NO_INDEX, 1);
        int intValue = increment.intValue();
        int i = intValue / length;
        char c = (char) (65 + i);
        String pre = String.valueOf(c);
        Long l = increment % length;
        StringBuilder no = new StringBuilder(l.toString());
        while (no.length() < 5) {
            no.insert(0, "0");
        }
        String userNo = pre + no.toString();
        redisTemplate.opsForValue().set(USER_NO + mobile, userNo);
        return userNo;
    }

    public String getUserNo(String mobile) {
        Object o = redisTemplate.opsForValue().get(USER_NO + mobile);
        return o == null ? null : o.toString();
    }

}

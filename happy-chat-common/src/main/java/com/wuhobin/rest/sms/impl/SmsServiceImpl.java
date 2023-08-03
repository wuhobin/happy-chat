package com.wuhobin.rest.sms.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuhobin.config.base.BasePlatformConfig;
import com.wuhobin.rest.sms.SmsService;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuhongbin
 * @description: 手机号验证码
 * @datetime 2023/01/17 11:21
 */
@SuppressWarnings("all")
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

   @Autowired
   private BasePlatformConfig basePlatformConfig;

    @Override
    public String sendCode(String mobile) {
        String code;
        try {
            ZhenziSmsClient client = new ZhenziSmsClient(basePlatformConfig.getSmsApiUrl(), basePlatformConfig.getSmsApiId(), basePlatformConfig.getSmsSecret());
            code = RandomStringUtils.randomNumeric(6);
            Map<String, Object> params = new HashMap<>(2);
            params.put("number", mobile);
            params.put("templateId", basePlatformConfig.getSmsTemplateId());
            String[] templateParams = new String[2];
            templateParams[0] = code;
            templateParams[1] = "5分钟";
            params.put("templateParams", templateParams);
            String result = client.send(params);
            JSONObject jsonObject = JSON.parseObject(result);
            log.info("【榛子短信验证码】 反参 jsonObject={}",jsonObject.toString());
            if (jsonObject.getIntValue("code") == 0){
               return code;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

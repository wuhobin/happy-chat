package com.wuhobin.config.base;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/3 11:44
 * @description
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "base.platform.config")
public class BasePlatformConfig {

    private String smsApiId;

    private String smsApiUrl;

    private String smsSecret;

    private String smsTemplateId;

    private String baiduAk;

}

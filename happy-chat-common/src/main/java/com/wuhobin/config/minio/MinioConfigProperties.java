package com.wuhobin.config.minio;

import io.minio.MinioClient;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/2 18:17
 * @description
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {

    private String url;

    private String accessKey;

    private String secretKey;

    private String bucketName;


    @Bean
    @SneakyThrows
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

}

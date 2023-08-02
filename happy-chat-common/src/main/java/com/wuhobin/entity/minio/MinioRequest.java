package com.wuhobin.entity.minio;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/2 18:33
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinioRequest {

    @ApiModelProperty(value = "上传的临时url")
    private String fileName;
}

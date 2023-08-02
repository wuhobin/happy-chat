package com.wuhobin.entity.minio;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
public class MinioResult {

    @ApiModelProperty(value = "上传的临时url")
    private String uploadUrl;


    @ApiModelProperty(value = "成功后能够下载的url")
    private String downloadUrl;

}

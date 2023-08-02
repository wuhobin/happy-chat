package com.wuhobin.happychatadmin.controller;


import com.wuhobin.api.CommonResult;
import com.wuhobin.config.minio.MinioTemplate;
import com.wuhobin.entity.minio.MinioResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description: oss控制层
 * Author: wuhongbin
 * Date: 2023-06-20
 */
@RestController
@RequestMapping("/api/oss")
@Api(tags = "oss相关接口")
public class OssController {
    @Autowired
    private MinioTemplate minioTemplate;

    @PostMapping("/upload")
    @ApiOperation("获取临时上传链接")
    public CommonResult<MinioResult> getUploadUrl(@RequestParam("file") MultipartFile file) {
        return CommonResult.success(minioTemplate.uploadFile(file));
    }
}

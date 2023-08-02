package com.wuhobin.config.minio;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.wuhobin.entity.minio.MinioRequest;
import com.wuhobin.entity.minio.MinioResult;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author wuhongbin
 * @version 1.0.0
 * @date 2023/8/2 18:28
 * @description
 */
@Component
public class MinioTemplate {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigProperties minioConfigProperties;

    /**
     * 查询所有存储桶
     *
     * @return Bucket 集合
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 桶是否存在
     *
     * @param bucketName 桶名
     * @return 是否存在
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }


    /**
     * 创建存储桶
     *
     * @param bucketName 桶名
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

        }
    }

    /**
     * 删除一个空桶 如果存储桶存在对象不为空时，删除会报错。
     *
     * @param bucketName 桶名
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows
    public MinioResult uploadFile(MultipartFile file){
        String fileName = generateFileName(file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfigProperties.getBucketName())
                .object(fileName)
                .stream(inputStream,inputStream.available(),-1)
                .contentType(file.getContentType())
                .build());
        return getPreSignedObjectUrl(MinioRequest.builder().fileName(fileName).build());
    }


    /**
     * 返回临时带签名、过期时间一天、Get请求方式的访问URL
     */
    @SneakyThrows
    public MinioResult getPreSignedObjectUrl(MinioRequest request) {
        String absolutePath =  generateAutoPath(request) ;
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(minioConfigProperties.getBucketName())
                        .object(request.getFileName())
                        .expiry(60 * 60 * 24)
                        .build());
        return MinioResult.builder()
                .uploadUrl(url)
                .downloadUrl(getDownloadUrl(minioConfigProperties.getBucketName(), request.getFileName()))
                .build();
    }

    private String generateAutoPath(MinioRequest request) {
        UUID uuid = UUID.fastUUID();
        String suffix = FileNameUtil.getSuffix(request.getFileName());
        String yearAndMonth = DateUtil.format(new Date(), DatePattern.NORM_MONTH_PATTERN);
        return StrUtil.SLASH + yearAndMonth + StrUtil.SLASH + uuid + StrUtil.DOT + suffix;
    }

    private String getDownloadUrl(String bucket, String fileName) {
        return minioConfigProperties.getUrl() + StrUtil.SLASH + bucket+ StrUtil.SLASH + fileName;
    }

    private String generateFileName(String originalFilename){
        String fileName = "";
        String[] split = originalFilename.split("\\.");
        if (split.length > 1) {
            fileName = System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(8) + "." + split[1];
        }else {
            fileName = System.currentTimeMillis() + RandomStringUtils.randomAlphabetic(8);
        }
        return fileName;
    }


}

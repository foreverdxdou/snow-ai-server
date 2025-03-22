package com.dxdou.snowai.service.impl;

import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.config.MinioConfig;
import com.dxdou.snowai.service.MinioService;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * MinIO服务实现类
 *
 * @author foreverdxdou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            // 检查存储桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .build());
            if (!found) {
                // 创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .build());
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extension;

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(uniqueFileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // 返回文件URL
            return getFileUrl(uniqueFileName);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            // 从URL中提取文件名
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            // 删除文件
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new BusinessException("文件删除失败：" + e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        try {
            // 生成预签名URL，有效期7天
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(fileName)
                    .method(Method.GET)
                    .expiry(7, TimeUnit.DAYS)
                    .build());
        } catch (Exception e) {
            log.error("获取文件URL失败", e);
            throw new BusinessException("获取文件URL失败：" + e.getMessage());
        }
    }
}
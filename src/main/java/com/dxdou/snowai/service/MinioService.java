package com.dxdou.snowai.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * MinIO服务接口
 *
 * @author foreverdxdou
 */
public interface MinioService {

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名
     * @return 文件URL
     */
    String uploadFile(MultipartFile file, String fileName);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl);

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    String getFileUrl(String fileName);
}
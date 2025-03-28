package com.dxdou.snowai.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dxdou.snowai.config.MinioConfig;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * 向量类型处理器
 *
 * @author foreverdxdou
 */
@Slf4j
@MappedJdbcTypes(value = {JdbcType.VARCHAR, JdbcType.CHAR})
@Component
public class MinioTypeHandler<T> extends BaseTypeHandler<T> {

    @Autowired(required = false)
    private MinioClient minioClient;
    @Autowired(required = false)
    private MinioConfig minioConfig;

    public static final String MINIO_SIGN_URL_PREFIX = "?X-Amz-Algorithm";
    public static final String SEPARATOR = "/";
    public static final int EXPIRATIONMINUTES = 60;


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        String url = (String) parameter;
        if (null != minioClient && url.contains(MINIO_SIGN_URL_PREFIX)) {
            url = url.substring(0, url.indexOf(MINIO_SIGN_URL_PREFIX));
        }
        if (null != minioClient && url.contains(minioConfig.getEndpoint())) {
            url = url.replaceAll(minioConfig.getEndpoint(), "");
        }
        ps.setString(i, url);
    }

    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);

        if (StringUtils.isBlank(columnValue)) {
            return (T) columnValue;
        }
        String url = minioConfig.getEndpoint() + generatePreSignedUrl(columnValue, minioConfig.getBucket());
        return (T) url;
    }


    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (StringUtils.isBlank(columnValue)) {
            return (T) columnValue;
        }
        String url = minioConfig.getEndpoint() + generatePreSignedUrl(columnValue, minioConfig.getBucket());
        return (T) url;
    }

    @SuppressWarnings("ALL")
    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (StringUtils.isBlank(columnValue)) {
            return (T) columnValue;
        }
        String url = minioConfig.getEndpoint() + generatePreSignedUrl(columnValue, minioConfig.getBucket());
        return (T) url;
    }


    public String generatePreSignedUrl(String url, String bucketName) {
        try {
            String objectName = null;
            if (org.apache.commons.lang3.StringUtils.isEmpty(url)) {
                return org.apache.commons.lang3.StringUtils.EMPTY;
            }

            if (url.contains(MINIO_SIGN_URL_PREFIX)) {
                url = url.substring(0, url.indexOf(MINIO_SIGN_URL_PREFIX));
            }
            url = url.substring(url.indexOf(bucketName));
            objectName = url.replace(bucketName + "/", "");

            // 设置访问凭证的过期时间
            ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(EXPIRATIONMINUTES);
            int seconds = (int) Duration.between(ZonedDateTime.now(), expiration).getSeconds();
            // 生成预签名 URL
            String signedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET) // 指定请求的 HTTP 方法
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(seconds)
                            .build()
            );

            return SEPARATOR + signedUrl.substring(signedUrl.indexOf(bucketName));
        } catch (Exception e) {
            log.error("生成预签名URL失败", e);
            return url;
        }
    }
}
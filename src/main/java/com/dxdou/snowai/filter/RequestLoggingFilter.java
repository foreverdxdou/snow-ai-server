package com.dxdou.snowai.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 请求日志过滤器
 *
 * @author foreverdxdou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 生成traceId
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put("traceId", traceId);

        // 检查是否是SSE请求
        boolean isSseRequest = isSseRequest(request);

        try {
            // 记录请求开始
            logRequest(request);

            if (isSseRequest) {
                // 对于SSE请求，直接使用原始响应
                filterChain.doFilter(request, response);
            } else {
                // 对于普通请求，使用包装器
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

                filterChain.doFilter(requestWrapper, responseWrapper);

                // 异步记录响应
                asyncLogResponse(requestWrapper, responseWrapper);

                // 复制响应内容到原始响应
                responseWrapper.copyBodyToResponse();
            }
        } finally {
            MDC.clear();
        }
    }

    @Async("asyncExecutor")
    protected void asyncLogResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        logResponse(request, response);
    }

    private boolean isSseRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("text/event-stream");
    }

    private void logRequest(HttpServletRequest request) {
        // 如果是文件上传或下载，不记录请求体
        if (isFileUploadOrDownload(request, null)) {
            Map<String, Object> requestInfo = new HashMap<>();
            requestInfo.put("method", request.getMethod());
            requestInfo.put("url", request.getRequestURI());
            requestInfo.put("queryString", request.getQueryString());
            requestInfo.put("contentType", request.getContentType());
            try {
                log.info("Request: File upload/download - {}", objectMapper.writeValueAsString(requestInfo));
            } catch (Exception e) {
                log.error("Failed to log request", e);
            }
            return;
        }

        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("method", request.getMethod());
        requestInfo.put("url", request.getRequestURI());
        requestInfo.put("queryString", request.getQueryString());

        // 记录请求头
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        requestInfo.put("headers", headers);

        // 记录请求参数
        Map<String, String[]> parameters = request.getParameterMap();
        requestInfo.put("parameters", parameters);

        // 记录请求体
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
            byte[] contentAsByteArray = requestWrapper.getContentAsByteArray();
            if (contentAsByteArray.length > 0) {
                String requestBody = new String(contentAsByteArray, StandardCharsets.UTF_8);
                if (!requestBody.isEmpty()) {
                    try {
                        // 尝试将请求体解析为JSON对象以便更好地格式化
                        Object json = objectMapper.readValue(requestBody, Object.class);
                        requestInfo.put("body", json);
                    } catch (Exception e) {
                        // 如果解析失败，直接使用原始字符串
                        requestInfo.put("body", requestBody);
                    }
                }
            }
        }

        try {
            log.info("Request: {}", objectMapper.writeValueAsString(requestInfo));
        } catch (Exception e) {
            log.error("Failed to log request", e);
        }
    }

    private void logResponse(HttpServletRequest request, ContentCachingResponseWrapper response) {
        // 如果是文件下载，不记录响应内容
        if (isFileUploadOrDownload(request, response)) {
            log.info("Response: File upload/download - {}", request.getRequestURI());
            return;
        }

        Map<String, Object> responseInfo = new HashMap<>();
        responseInfo.put("status", response.getStatus());

        // 记录响应头
        Map<String, String> headers = new HashMap<>();
        response.getHeaderNames().forEach(headerName -> headers.put(headerName, response.getHeader(headerName)));
        responseInfo.put("headers", headers);

        // 记录响应体
        byte[] contentAsByteArray = response.getContentAsByteArray();
        if (contentAsByteArray.length > 0) {
            String responseBody = new String(contentAsByteArray, StandardCharsets.UTF_8);
            if (!responseBody.isEmpty()) {
                try {
                    // 尝试将响应体解析为JSON对象以便更好地格式化
                    Object json = objectMapper.readValue(responseBody, Object.class);
                    responseInfo.put("body", json);
                } catch (Exception e) {
                    // 如果解析失败，直接使用原始字符串
                    responseInfo.put("body", responseBody);
                }
            }
        }

        try {
            log.info("Response: {}", objectMapper.writeValueAsString(responseInfo));
        } catch (Exception e) {
            log.error("Failed to log response", e);
        }
    }

    private boolean isFileUploadOrDownload(HttpServletRequest request, HttpServletResponse response) {
        String contentType = null;
        if (response != null) {
            contentType = response.getContentType();
        } else {
            contentType = request.getContentType();
        }

        if (contentType == null) {
            return false;
        }

        // 文件上传的Content-Type
        boolean isUpload = contentType.contains("multipart/form-data") ||
                contentType.contains("application/x-www-form-urlencoded");

        // 文件下载的Content-Type
        boolean isDownload = contentType.contains("application/octet-stream") ||
                contentType.contains("application/pdf") ||
                contentType.contains("application/msword") ||
                contentType.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                contentType.contains("application/vnd.ms-excel") ||
                contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                contentType.contains("application/vnd.ms-powerpoint") ||
                contentType.contains("application/vnd.openxmlformats-officedocument.presentationml.presentation") ||
                contentType.contains("image/") ||
                contentType.contains("video/") ||
                contentType.contains("audio/") ||
                contentType.contains("text/csv") ||
                contentType.contains("text/plain") ||
                contentType.contains("application/zip") ||
                contentType.contains("application/x-rar-compressed") ||
                contentType.contains("application/x-7z-compressed") ||
                contentType.contains("text/event-stream");

        return isUpload || isDownload;
    }
}
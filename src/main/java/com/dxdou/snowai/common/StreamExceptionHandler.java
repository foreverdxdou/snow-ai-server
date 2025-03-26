package com.dxdou.snowai.common;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * 流式响应异常处理器
 *
 * @author foreverdxdou
 */
@Slf4j
@ControllerAdvice
public class StreamExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(HttpServletResponse response,
                                   AccessDeniedException ex) throws IOException {
        if (!response.isCommitted()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        } else {
            log.warn("响应已提交，无法发送错误状态: {}", ex.getMessage());
            response.getWriter().write("ERROR:" + ex.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public void handleGeneralException(HttpServletResponse response,
                                       Exception ex) throws IOException {
        if (!response.isCommitted()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        } else {
            log.error("流式响应异常: {}", ex.getMessage());
            response.getWriter().write("ERROR:" + ex.getMessage());
            response.flushBuffer();
        }
    }
}

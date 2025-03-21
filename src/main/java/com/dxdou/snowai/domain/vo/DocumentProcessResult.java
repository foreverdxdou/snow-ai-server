package com.dxdou.snowai.domain.vo;

import lombok.Data;

/**
 * 文档处理结果VO类
 *
 * @author foreverdxdou
 */
@Data
public class DocumentProcessResult {

    /**
     * 文档摘要
     */
    private String summary;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 处理状态
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}
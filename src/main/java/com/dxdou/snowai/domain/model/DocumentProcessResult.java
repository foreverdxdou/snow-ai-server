package com.dxdou.snowai.domain.model;

import lombok.Data;

/**
 * 文档处理结果
 */
@Data
public class DocumentProcessResult {
    /**
     * 文档摘要
     */
    private String summary;

    /**
     * 关键词列表
     */
    private String keywords;

    /**
     * 处理是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}
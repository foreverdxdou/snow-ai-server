package com.dxdou.snowai.service;

import com.dxdou.snowai.domain.model.DocumentProcessResult;

/**
 * 知识库文档处理服务接口
 *
 * @author foreverdxdou
 */
public interface KbDocumentProcessService {

    /**
     * 生成文档摘要
     *
     * @param content   文档内容
     * @param maxLength 摘要最大长度
     * @return 文档摘要
     */
    String generateSummary(String content, int maxLength);

    /**
     * 提取文档关键词
     *
     * @param content     文档内容
     * @param maxKeywords 关键词最大数量
     * @return 关键词列表
     */
    String extractKeywords(String content, int maxKeywords);

    /**
     * 处理文档内容
     *
     * @param content          文档内容
     * @param maxSummaryLength 摘要最大长度
     * @param maxKeywords      关键词最大数量
     * @return 处理结果
     */
    DocumentProcessResult processDocument(String content, int maxSummaryLength, int maxKeywords);
}
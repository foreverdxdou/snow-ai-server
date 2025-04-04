package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbSearchVO;

import java.util.List;

/**
 * 知识库搜索服务接口
 *
 * @author foreverdxdou
 */
public interface KbSearchService {

    /**
     * 语义搜索
     *
     * @param query      查询文本
     * @param kbIds      知识库ID列表
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> semanticSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * 关键词搜索
     *
     * @param query      查询文本
     * @param kbIds      知识库ID列表
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> keywordSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * 混合搜索（结合语义搜索和关键词搜索）
     *
     * @param query      查询文本
     * @param kbIds      知识库ID列表
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> hybridSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * Embedding向量搜索
     *
     * @param query      查询文本
     * @param kbIds      知识库ID列表
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> embeddingSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * 获取文档相似度
     *
     * @param docId1 文档1 ID
     * @param docId2 文档2 ID
     * @return 相似度分数
     */
    double getDocumentSimilarity(Long docId1, Long docId2);

    /**
     * 更新文档向量
     *
     * @param docId        文档ID
     * @param useEmbedding 是否使用Embedding
     */
    void updateDocumentVector(Long docId, boolean useEmbedding);

    /**
     * 生成文本的Embedding向量
     *
     * @param text 文本内容
     * @return 向量数组
     */
    float[] generateEmbeddingVector(String text);
}
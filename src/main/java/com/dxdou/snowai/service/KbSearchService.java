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
     * @param kbId       知识库ID
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> semanticSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * 关键词搜索
     *
     * @param query      查询文本
     * @param kbId       知识库ID
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> keywordSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> excludeIds);

    /**
     * 混合搜索（结合语义搜索和关键词搜索）
     *
     * @param query      查询文本
     * @param kbId       知识库ID
     * @param page       分页参数
     * @param excludeIds 排除的文档ID列表
     * @return 搜索结果
     */
    Page<KbSearchVO> hybridSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> excludeIds);

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
     * @param docId 文档ID
     */
    void updateDocumentVector(Long docId);
}
package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.KbDocumentVector;
import com.dxdou.snowai.domain.vo.KbSearchVO;
import com.dxdou.snowai.mapper.KbDocumentMapper;
import com.dxdou.snowai.mapper.KbDocumentVectorMapper;
import com.dxdou.snowai.service.KbSearchService;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 知识库智能搜索服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbSearchServiceImpl extends ServiceImpl<KbDocumentMapper, KbDocument> implements KbSearchService {

    private final KbDocumentMapper documentMapper;
    private final KbDocumentVectorMapper documentVectorMapper;
    private final StanfordCoreNLP nlpPipeline;

    @Override
    public Page<KbSearchVO> semanticSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> tagIds) {
        // 1. 获取查询词的向量表示
        float[] queryVector = getQueryVector(query);

        // 2. 从向量数据库中检索相似文档
        List<KbDocumentVector> similarVectors = documentVectorMapper.findSimilarVectors(queryVector, kbId,
                page.getSize());

        // 3. 获取文档ID列表
        List<Long> docIds = similarVectors.stream()
                .map(KbDocumentVector::getDocumentId)
                .collect(Collectors.toList());

        // 4. 获取文档详情
        List<KbDocument> documents = documentMapper.selectBatchIds(docIds);

        // 5. 转换为VO对象
        List<KbSearchVO> searchResults = documents.stream()
                .map(this::convertToSearchVO)
                .collect(Collectors.toList());

        // 6. 设置相似度分数
        for (int i = 0; i < searchResults.size(); i++) {
            searchResults.get(i).setSimilarity(similarVectors.get(i).getSimilarity());
        }

        // 7. 构建分页结果
        page.setRecords(searchResults);
        page.setTotal(documents.size());

        return page;
    }

    @Override
    public Page<KbSearchVO> keywordSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> tagIds) {
        // 1. 构建查询条件
        LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbDocument::getKbId, kbId)
                .and(w -> w.like(KbDocument::getTitle, query)
                        .or()
                        .like(KbDocument::getContent, query));

        // 2. 添加标签过滤
        if (!CollectionUtils.isEmpty(tagIds)) {
            wrapper.exists("SELECT 1 FROM kb_document_tag dt WHERE dt.document_id = kb_document.id AND dt.tag_id IN (" +
                    String.join(",", tagIds.stream().map(String::valueOf).collect(Collectors.toList())) + ")");
        }

        // 3. 执行查询
        Page<KbDocument> documentPage = new Page<>(page.getCurrent(), page.getSize());
        documentPage = documentMapper.selectPage(documentPage, wrapper);

        // 4. 转换为VO对象
        List<KbSearchVO> searchResults = documentPage.getRecords().stream()
                .map(this::convertToSearchVO)
                .collect(Collectors.toList());

        // 5. 构建分页结果
        page.setRecords(searchResults);
        page.setTotal(documentPage.getTotal());

        return page;
    }

    @Override
    public Page<KbSearchVO> hybridSearch(String query, Long kbId, Page<KbSearchVO> page, List<Long> tagIds) {
        // 1. 获取语义搜索结果
        Page<KbSearchVO> semanticResults = semanticSearch(query, kbId, page, tagIds);

        // 2. 获取关键词搜索结果
        Page<KbSearchVO> keywordResults = keywordSearch(query, kbId, page, tagIds);

        // 3. 合并结果并去重
        Set<Long> docIds = new HashSet<>();
        List<KbSearchVO> mergedResults = new ArrayList<>();

        // 添加语义搜索结果
        for (KbSearchVO result : semanticResults.getRecords()) {
            if (docIds.add(result.getId())) {
                mergedResults.add(result);
            }
        }

        // 添加关键词搜索结果
        for (KbSearchVO result : keywordResults.getRecords()) {
            if (docIds.add(result.getId())) {
                result.setSimilarity(0.5); // 设置一个默认的相似度分数
                mergedResults.add(result);
            }
        }

        // 4. 按相似度排序
        mergedResults.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

        // 5. 构建分页结果
        page.setRecords(mergedResults);
        page.setTotal(mergedResults.size());

        return page;
    }

    @Override
    public double getDocumentSimilarity(Long docId1, Long docId2) {
        // 1. 获取两个文档的向量
        KbDocumentVector vector1 = documentVectorMapper.selectByDocumentId(docId1);
        KbDocumentVector vector2 = documentVectorMapper.selectByDocumentId(docId2);

        if (vector1 == null || vector2 == null) {
            return 0.0;
        }

        // 2. 计算余弦相似度
        float[] v1 = vector1.getContentVector();
        float[] v2 = vector2.getContentVector();
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocumentVector(Long docId) {
        // 1. 获取文档内容
        KbDocument document = documentMapper.selectById(docId);
        if (document == null || !StringUtils.hasText(document.getContent())) {
            return;
        }

        // 2. 生成文档向量
        float[] vector = generateDocumentVector(document.getContent());

        // 3. 更新向量数据库
        KbDocumentVector documentVector = new KbDocumentVector();
        documentVector.setDocumentId(docId);
        documentVector.setContentVector(vector);
        documentVector.setChunkIndex(0);
        documentVector.setChunkContent(document.getContent());

        documentVectorMapper.insertOrUpdate(documentVector);
    }

    /**
     * 获取查询词的向量表示
     *
     * @param query 查询词
     * @return 向量表示
     */
    private float[] getQueryVector(String query) {
        // 使用NLP模型生成查询词的向量表示
        // 这里需要根据实际使用的模型来实现
        return new float[1536];
    }

    /**
     * 生成文档向量
     *
     * @param content 文档内容
     * @return 向量表示
     */
    private float[] generateDocumentVector(String content) {
        // 使用NLP模型生成文档的向量表示
        // 这里需要根据实际使用的模型来实现
        return new float[1536];
    }

    /**
     * 将文档实体转换为搜索结果VO
     *
     * @param document 文档实体
     * @return 搜索结果VO
     */
    private KbSearchVO convertToSearchVO(KbDocument document) {
        KbSearchVO vo = new KbSearchVO();
        // 设置基本属性
        vo.setId(document.getId());
        vo.setTitle(document.getTitle());
        vo.setContent(document.getContent());
        vo.setFileType(document.getFileType());
        vo.setFileSize(document.getFileSize());
        vo.setFileUrl(document.getFileUrl());
        vo.setCategoryId(document.getCategoryId());
        vo.setKbId(document.getKbId());
        vo.setCreatorId(document.getCreatorId());
        vo.setVersion(document.getVersion());
        vo.setCreateTime(document.getCreateTime());
        vo.setUpdateTime(document.getUpdateTime());

        // TODO: 设置其他属性（如分类名称、知识库名称、创建者名称、标签列表等）

        return vo;
    }
}
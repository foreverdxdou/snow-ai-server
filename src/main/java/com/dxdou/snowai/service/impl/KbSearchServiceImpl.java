package com.dxdou.snowai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.KbDocumentIndex;
import com.dxdou.snowai.domain.entity.KbDocumentVector;
import com.dxdou.snowai.domain.vo.EmbeddingConfigVO;
import com.dxdou.snowai.domain.vo.KbSearchVO;
import com.dxdou.snowai.mapper.KbDocumentMapper;
import com.dxdou.snowai.mapper.KbDocumentVectorMapper;
import com.dxdou.snowai.service.EmbeddingConfigService;
import com.dxdou.snowai.service.KbSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 知识库智能搜索服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KbSearchServiceImpl extends ServiceImpl<KbDocumentMapper, KbDocument> implements KbSearchService {

    private final EmbeddingConfigService embeddingConfigService;
    private final KbDocumentMapper documentMapper;
    private final KbDocumentVectorMapper documentVectorMapper;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ElasticsearchOperations elasticsearchTemplate;
    private static final String IDF_CACHE_PREFIX = "kb:idf:";
    private static final int IDF_CACHE_EXPIRE = 24 * 60 * 60; // 24小时
    private static final String QUERY_VECTOR_CACHE_PREFIX = "kb:query:vector:";
    private static final int QUERY_VECTOR_CACHE_EXPIRE = 60 * 60; // 1小时
    private static final int CHUNK_SIZE = 500; // 每个文档块的最大字符数
    private static final int CHUNK_OVERLAP = 100; // 块之间的重叠字符数

    @Override
    public Page<KbSearchVO> semanticSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> tagIds) {
        // 使用ElasticsearchOperations根据关键词检索文档KbDocumentIndex
        Criteria criteria = new Criteria().or("content").matches(query)
                .or("title").matches(query);
        // 假设在 content 字段中搜索
        Query searchQuery = CriteriaQuery.builder(criteria).withMinScore(3).withPageable(
                Pageable.ofSize((int) page.getSize())).build();

        SearchHits<KbDocumentIndex> searchHits = elasticsearchTemplate.search(searchQuery, KbDocumentIndex.class);

        List<KbDocumentIndex> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        Set<Long> docIds = new HashSet<>();
        if (CollUtil.isNotEmpty(results)) {
            docIds = results.stream().map(kbDocumentIndex -> Long.valueOf(kbDocumentIndex.getId())).collect(Collectors.toSet());
        }


        // 1. 获取查询词的向量表示
        float[] queryVector = getQueryVector(query);

        // 2. 从向量数据库中检索相似文档块
        List<KbDocumentVector> similarVectors = documentVectorMapper.findSimilarVectors(queryVector, kbIds,
                docIds, page.getSize() * 3); // 获取更多结果以便后续处理

        // 3. 按文档ID分组，选择最相似的块
        Map<Long, KbDocumentVector> bestChunks = similarVectors.stream()
                .collect(Collectors.groupingBy(
                        KbDocumentVector::getDocumentId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                chunks -> chunks.stream()
                                        .max(Comparator.comparingDouble(KbDocumentVector::getSimilarity))
                                        .orElse(null))));

        // 4. 获取文档详情
        List<KbDocument> documents = documentMapper.selectBatchIds(bestChunks.keySet());

        // 5. 转换为VO对象并设置相关块内容
        List<KbSearchVO> searchResults = documents.stream()
                .map(doc -> {
                    KbSearchVO vo = convertToSearchVO(doc);
                    KbDocumentVector bestChunk = bestChunks.get(doc.getId());
                    if (bestChunk != null) {
                        vo.setSimilarity(bestChunk.getSimilarity());
                        vo.setMatchedContent(bestChunk.getChunkContent()); // 假设KbSearchVO有这个字段
                    }
                    return vo;
                })
                .sorted(Comparator.comparingDouble(KbSearchVO::getSimilarity).reversed())
                .limit(page.getSize())
                .collect(Collectors.toList());

        // 6. 构建分页结果
        page.setRecords(searchResults);
        page.setTotal(searchResults.size());

        return page;
    }

    @Override
    public Page<KbSearchVO> keywordSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> tagIds) {
        // 1. 构建查询条件
        LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(KbDocument::getKbId, kbIds)
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
    public Page<KbSearchVO> hybridSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> tagIds) {
        // 1. 获取语义搜索结果
        Page<KbSearchVO> semanticResults = semanticSearch(query, kbIds, page, tagIds);

        // 2. 获取关键词搜索结果
        Page<KbSearchVO> keywordResults = keywordSearch(query, kbIds, page, tagIds);

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
    public void updateDocumentVector(Long docId, boolean useEmbedding) {
        // 1. 获取文档内容
        KbDocument document = documentMapper.selectById(docId);
        if (document == null || !StringUtils.hasText(document.getContent())) {
            return;
        }

        // 2. 删除文档现有的向量
        documentVectorMapper.deleteByDocumentId(docId);

//        // 3. 将文档内容分块
//        List<TextChunkUtil.DocumentChunk> chunks = TextChunkUtil.chunkContent(document.getContent());
//
//        // 4. 为每个块生成向量并保存
//        for (int i = 0; i < chunks.size(); i++) {
//            TextChunkUtil.DocumentChunk documentChunk = chunks.get(i);
        // 3. 将文档内容分块
        List<DocumentChunk> chunks = splitDocumentIntoChunks(document.getContent());

        // 4. 为每个块生成向量并保存
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk documentChunk = chunks.get(i);
            float[] vector;

            if (useEmbedding) {
                // 使用Embedding生成向量
                vector = generateEmbeddingVector(documentChunk.content);
            } else {
                // 使用NLP生成向量
                vector = generateDocumentVector(documentChunk.content);
            }

            KbDocumentVector documentVector = new KbDocumentVector();
            documentVector.setDocumentId(docId);
            documentVector.setContentVector(vector);
            documentVector.setChunkIndex(i);
            documentVector.setChunkContent(documentChunk.content);

            documentVectorMapper.insert(documentVector);
        }
    }

    private static class DocumentChunk {
        int index;
        String content;

        DocumentChunk(int index, String content) {
            this.index = index;
            this.content = content;
        }
    }

    private List<DocumentChunk> splitDocumentIntoChunks(String content) {
        List<DocumentChunk> chunks = new ArrayList<>();

        // 1. 首先按自然段落分割
        String[] paragraphs = content.split("\n\n");

        StringBuilder currentChunk = new StringBuilder();
        int currentIndex = 0;

        for (String paragraph : paragraphs) {
            // 如果当前块加上新段落超过了块大小
            if (currentChunk.length() + paragraph.length() > CHUNK_SIZE) {
                // 保存当前块
                if (currentChunk.length() > 0) {
                    chunks.add(new DocumentChunk(currentIndex++, currentChunk.toString().trim()));

                    // 保留最后一部分作为重叠
                    if (currentChunk.length() > CHUNK_OVERLAP) {
                        String overlap = currentChunk.substring(
                                Math.max(0, currentChunk.length() - CHUNK_OVERLAP));
                        currentChunk = new StringBuilder(overlap);
                    } else {
                        currentChunk = new StringBuilder();
                    }
                }
            }

            // 添加新段落
            if (currentChunk.length() > 0) {
                currentChunk.append("\n\n");
            }
            currentChunk.append(paragraph);
        }

        // 添加最后一个块
        if (currentChunk.length() > 0) {
            chunks.add(new DocumentChunk(currentIndex, currentChunk.toString().trim()));
        }

        return chunks;
    }

    /**
     * 获取查询词的向量表示
     *
     * @param query 查询词
     * @return 向量表示
     */
    private float[] getQueryVector(String query) {
        try {
            // 检查缓存
            String cacheKey = QUERY_VECTOR_CACHE_PREFIX + query;
            Object cachedVector = redisTemplate.opsForValue().get(cacheKey);
            if (cachedVector != null) {
                return objectMapper.convertValue(cachedVector, float[].class);
            }

            // 生成向量的现有实现...
            float[] vector = generateDocumentVector(query);

            // 缓存结果
            redisTemplate.opsForValue().set(cacheKey, vector, QUERY_VECTOR_CACHE_EXPIRE, TimeUnit.SECONDS);

            return vector;
        } catch (Exception e) {
            log.error("获取查询向量失败，query: {}", query, e);
            return new float[1536];
        }
    }

    private float[] generateDocumentVector(String text) {
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            return new float[1536];
        }

        try {
            // 创建注释对象
            edu.stanford.nlp.pipeline.CoreDocument document = new edu.stanford.nlp.pipeline.CoreDocument(text);

            // 使用 nlpPipeline 处理文档
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
            props.setProperty("coref.algorithm", "neural");
            StanfordCoreNLP nlpPipeline = new StanfordCoreNLP(props);
            nlpPipeline.annotate(document);

            // 获取所有词元（lemmas）
            List<String> lemmas = new ArrayList<>();
            for (edu.stanford.nlp.pipeline.CoreSentence sentence : document.sentences()) {
                lemmas.addAll(sentence.lemmas());
            }

            // 使用 TF-IDF 计算特征向量
            float[] vector = new float[1536];
            Map<String, Integer> termFrequency = new HashMap<>();

            // 计算词频
            for (String lemma : lemmas) {
                termFrequency.merge(lemma, 1, Integer::sum);
            }

            // 计算 TF-IDF 值并填充向量
            int vectorIndex = 0;
            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                if (vectorIndex >= 1536)
                    break;

                String term = entry.getKey();
                int freq = entry.getValue();

                // 计算 TF-IDF
                float tf = (float) freq / lemmas.size();
                float idf = calculateIDF(term);
                vector[vectorIndex++] = tf * idf;
            }

            // 向量归一化
            float magnitude = 0;
            for (float v : vector) {
                magnitude += v * v;
            }
            magnitude = (float) Math.sqrt(magnitude);

            if (magnitude > 0) {
                for (int i = 0; i < vector.length; i++) {
                    vector[i] = vector[i] / magnitude;
                }
            }

            return vector;

        } catch (Exception e) {
            log.error("生成查询向量失败", e);
            return new float[1536];
        }
    }

    private float calculateIDF(String term) {
        try {
            // 1. 先从缓存中获取IDF值
            String cacheKey = IDF_CACHE_PREFIX + term;
            Object cachedIdf = redisTemplate.opsForValue().get(cacheKey);
            if (cachedIdf != null) {
                return Float.parseFloat(cachedIdf.toString());
            }

            // 2. 获取总文档数
            long totalDocuments = documentMapper.selectCount(null);
            if (totalDocuments == 0) {
                return 1.0f;
            }

            // 3. 获取包含该词的文档数
            LambdaQueryWrapper<KbDocument> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(KbDocument::getContent, term);
            long documentsWithTerm = documentMapper.selectCount(wrapper);

            // 4. 计算IDF值
            // 使用平滑处理，避免除以零的情况
            float idf = (float) Math.log10((double) totalDocuments / (documentsWithTerm + 1));

            // 5. 缓存计算结果
            redisTemplate.opsForValue().set(cacheKey, idf, IDF_CACHE_EXPIRE, TimeUnit.SECONDS);

            return idf;
        } catch (Exception e) {
            log.error("计算IDF值失败，term: {}", term, e);
            return 1.0f; // 发生错误时返回默认值
        }
    }

    // 批量更新IDF缓存的方法
//    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点执行
//    public void updateIDFCache() {
//        try {
//            // 1. 获取所有文档的词元
//            List<KbDocument> documents = documentMapper.selectList(null);
//            long totalDocuments = documents.size();
//
//            // 2. 统计每个词出现在多少文档中
//            Map<String, Long> termDocumentFrequency = new HashMap<>();
//
//            Properties props = new Properties();
//            props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
//            props.setProperty("coref.algorithm", "neural");
//            StanfordCoreNLP nlpPipeline = new StanfordCoreNLP(props);
//
//            for (KbDocument doc : documents) {
//                if (StringUtils.hasText(doc.getContent())) {
//                    // 使用 nlpPipeline 处理文档
//                    edu.stanford.nlp.pipeline.CoreDocument document = new edu.stanford.nlp.pipeline.CoreDocument(
//                            doc.getContent());
//                    nlpPipeline.annotate(document);
//
//                    // 获取文档中的唯一词元
//                    Set<String> uniqueTerms = document.sentences().stream()
//                            .flatMap(sentence -> sentence.lemmas().stream())
//                            .collect(Collectors.toSet());
//
//                    // 更新词频统计
//                    uniqueTerms.forEach(term -> termDocumentFrequency.merge(term, 1L, Long::sum));
//                }
//            }
//
//            // 3. 计算并缓存IDF值
//            termDocumentFrequency.forEach((term, frequency) -> {
//                float idf = (float) Math.log10((double) totalDocuments / (frequency + 1));
//                String cacheKey = IDF_CACHE_PREFIX + term;
//                redisTemplate.opsForValue().set(cacheKey, idf, IDF_CACHE_EXPIRE, TimeUnit.SECONDS);
//            });
//
//            log.info("IDF缓存更新完成，共处理 {} 个词元", termDocumentFrequency.size());
//        } catch (Exception e) {
//            log.error("更新IDF缓存失败", e);
//        }
//    }

    @Override
    public Page<KbSearchVO> embeddingSearch(String query, Long[] kbIds, Page<KbSearchVO> page, List<Long> excludeIds) {
        // 使用ElasticsearchOperations根据关键词检索文档KbDocumentIndex
        Criteria criteria = new Criteria().or("content").matches(query)
                .or("title").matches(query);
        Query searchQuery = CriteriaQuery.builder(criteria).withPageable(
                Pageable.ofSize((int) page.getSize())).build();

        SearchHits<KbDocumentIndex> searchHits = elasticsearchTemplate.search(searchQuery, KbDocumentIndex.class);

        List<KbDocumentIndex> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        Set<Long> docIds = new HashSet<>();
        if (CollUtil.isNotEmpty(results)) {
            docIds = results.stream().map(kbDocumentIndex -> Long.valueOf(kbDocumentIndex.getId())).collect(Collectors.toSet());
        }

        // 1. 获取查询文本的向量表示
        float[] queryVector = generateEmbeddingVector(query);

        // 2. 从向量数据库中检索相似文档块
        List<KbDocumentVector> similarVectors = documentVectorMapper.findSimilarVectors(queryVector, kbIds, docIds,
                page.getSize() * 3); // 获取更多结果以便后续处理

        // 3. 按文档ID分组，选择最相似的块
        Map<Long, KbDocumentVector> bestChunks = similarVectors.stream()
                .filter(v -> excludeIds == null || !excludeIds.contains(v.getDocumentId()))
                .collect(Collectors.groupingBy(
                        KbDocumentVector::getDocumentId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                chunks -> chunks.stream()
                                        .max(Comparator.comparingDouble(KbDocumentVector::getSimilarity))
                                        .orElse(null))));

        // 4. 获取文档详情
        List<KbDocument> documents = documentMapper.selectBatchIds(bestChunks.keySet());

        // 5. 转换为VO对象并设置相关块内容
        List<KbSearchVO> searchResults = documents.stream()
                .map(doc -> {
                    KbSearchVO vo = convertToSearchVO(doc);
                    KbDocumentVector bestChunk = bestChunks.get(doc.getId());
                    if (bestChunk != null) {
                        vo.setSimilarity(bestChunk.getSimilarity());
                        vo.setMatchedContent(bestChunk.getChunkContent());
                    }
                    return vo;
                })
                .sorted(Comparator.comparingDouble(KbSearchVO::getSimilarity).reversed())
                .limit(1)
                .collect(Collectors.toList());

        // 6. 构建分页结果
        page.setRecords(searchResults);
        page.setTotal(searchResults.size());

        return page;
    }

    @Override
    public float[] generateEmbeddingVector(String text) {
        if (!StringUtils.hasText(text)) {
            return new float[1536];
        }

        EmbeddingConfigVO embeddingConfig = embeddingConfigService.getEnabledEmbeddingConfig();

        try {
            // 构造请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("input", text);
            requestBody.put("model", embeddingConfig.getModelType());

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(embeddingConfig.getApiKey());

            // 发送请求
            String response = HttpUtil.createPost(embeddingConfig.getBaseUrl())
                    .bearerAuth(embeddingConfig.getApiKey())
                    .body(requestBody.toJSONString())
                    .execute()
                    .body();
            log.info("Embedding API调用结果: {}", response);

            List<Double> embeddingList = new ArrayList<>();
            // 解析响应
            JSONObject responseJson = JSON.parseObject(response);
            if (responseJson.containsKey("data")) {
                // 处理"data"字段的情况
                JSONArray dataArray = responseJson.getJSONArray("data");
                if (dataArray != null && !dataArray.isEmpty()) {
                    JSONArray embeddingArray = dataArray.getJSONObject(0).getJSONArray("embedding");
                    if (embeddingArray != null) {
                        embeddingList = embeddingArray.toJavaList(Double.class);
                    }
                }
            } else if (responseJson.containsKey("embeddings")) {
                // 处理"embeddings"字段的情况
                JSONArray embeddingsArray = responseJson.getJSONArray("embeddings");
                if (embeddingsArray != null && !embeddingsArray.isEmpty()) {
                    JSONArray embeddingArray = embeddingsArray.getJSONArray(0);
                    if (embeddingArray != null) {
                        embeddingList = embeddingArray.toJavaList(Double.class);
                    }
                }
            }

            int dimensions = embeddingList.size();
            if (dimensions < 1536) {
                dimensions = 1536;
            }
            // 转换List<Double>为float[]
            float[] vector = new float[dimensions];
            for (int i = 0; i < embeddingList.size(); i++) {
                vector[i] = embeddingList.get(i).floatValue();
            }
            return vector;

        } catch (Exception e) {
            log.error("Embedding API调用失败: {}", e.getMessage());
            throw new BusinessException("Embedding API调用失败", e);
        }

//        // 降级方案：返回随机向量
//        return generateRandomVector(embeddingConfig.getDimensions());
    }

    private float[] generateRandomVector(int dimension) {
        float[] vector = new float[dimension];
        Random rand = new Random();
        for (int i = 0; i < dimension; i++) {
            vector[i] = rand.nextFloat() * 2 - 1; // 生成-1到1之间的随机数
        }
        return vector;
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
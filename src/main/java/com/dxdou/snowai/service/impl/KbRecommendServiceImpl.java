package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.KbUserBehavior;
import com.dxdou.snowai.domain.vo.KbSearchVO;
import com.dxdou.snowai.mapper.KbDocumentMapper;
import com.dxdou.snowai.mapper.KbUserBehaviorMapper;
import com.dxdou.snowai.service.KbRecommendService;
import com.dxdou.snowai.service.KbSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 知识库推荐服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbRecommendServiceImpl extends ServiceImpl<KbUserBehaviorMapper, KbUserBehavior>
        implements KbRecommendService {

    private final KbDocumentMapper documentMapper;
    private final KbSearchService searchService;
    private final KbUserBehaviorMapper userBehaviorMapper;

    @Override
    public Page<KbSearchVO> getPersonalRecommendations(Long userId, Long kbId, Page<KbSearchVO> page) {
        // 1. 获取用户行为统计
        Map<String, Object> stats = getUserBehaviorStats(userId, kbId);

        // 2. 根据用户行为类型权重计算推荐分数
        List<KbDocument> documents = documentMapper.selectList(
                new LambdaQueryWrapper<KbDocument>()
                        .eq(KbDocument::getKbId, kbId));

        // 3. 计算每个文档的推荐分数
        Map<Long, Double> docScores = new HashMap<>();
        for (KbDocument doc : documents) {
            double score = calculatePersonalScore(doc.getId(), stats);
            docScores.put(doc.getId(), score);
        }

        // 4. 按分数排序并返回结果
        List<KbDocument> recommendedDocs = documents.stream()
                .sorted((a, b) -> Double.compare(docScores.get(b.getId()), docScores.get(a.getId())))
                .limit(page.getSize())
                .collect(Collectors.toList());

        // 5. 转换为VO对象
        List<KbSearchVO> records = recommendedDocs.stream()
                .map(this::convertToSearchVO)
                .collect(Collectors.toList());

        page.setRecords(records);
        page.setTotal(documents.size());
        return page;
    }

    @Override
    public Page<KbSearchVO> getHotRecommendations(Long kbId, Page<KbSearchVO> page) {
        // 1. 获取最近7天的热门文档
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<KbUserBehavior> recentBehaviors = userBehaviorMapper.selectList(
                new LambdaQueryWrapper<KbUserBehavior>()
                        .eq(KbUserBehavior::getKbId, kbId)
                        .ge(KbUserBehavior::getCreateTime, sevenDaysAgo));

        // 2. 统计文档访问次数
        Map<Long, Long> docVisitCounts = recentBehaviors.stream()
                .collect(Collectors.groupingBy(KbUserBehavior::getDocId, Collectors.counting()));

        // 3. 获取热门文档
        List<KbDocument> hotDocs = documentMapper.selectList(
                new LambdaQueryWrapper<KbDocument>()
                        .eq(KbDocument::getKbId, kbId)
                        .in(KbDocument::getId, docVisitCounts.keySet()));

        // 4. 按访问次数排序
        List<KbDocument> sortedDocs = hotDocs.stream()
                .sorted((a, b) -> Long.compare(docVisitCounts.get(b.getId()), docVisitCounts.get(a.getId())))
                .limit(page.getSize())
                .collect(Collectors.toList());

        // 5. 转换为VO对象
        List<KbSearchVO> records = sortedDocs.stream()
                .map(this::convertToSearchVO)
                .collect(Collectors.toList());

        page.setRecords(records);
        page.setTotal(hotDocs.size());
        return page;
    }

    @Override
    public Page<KbSearchVO> getRelatedRecommendations(Long docId, Long[] kbIds, Page<KbSearchVO> page) {
        // 1. 获取当前文档
        KbDocument currentDoc = documentMapper.selectById(docId);
        if (currentDoc == null) {
            return page;
        }

        // 2. 使用语义搜索查找相关文档
        Page<KbSearchVO> searchPage = searchService.semanticSearch(currentDoc.getContent(), kbIds, page, null);

        // 3. 过滤掉当前文档
        List<KbSearchVO> relatedDocs = searchPage.getRecords().stream()
                .filter(doc -> !doc.getId().equals(docId))
                .limit(page.getSize())
                .collect(Collectors.toList());

        page.setRecords(relatedDocs);
        page.setTotal(searchPage.getTotal() - 1);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordUserBehavior(Long userId, Long docId, String behaviorType) {
        // 1. 获取文档信息
        KbDocument document = documentMapper.selectById(docId);
        if (document == null) {
            return;
        }

        // 2. 记录用户行为
        KbUserBehavior behavior = new KbUserBehavior();
        behavior.setUserId(userId);
        behavior.setKbId(document.getKbId());
        behavior.setDocId(docId);
        behavior.setBehaviorType(behaviorType);
        behavior.setCreateTime(LocalDateTime.now());

        userBehaviorMapper.insert(behavior);
    }

    @Override
    public Map<String, Object> getUserBehaviorStats(Long userId, Long kbId) {
        // 1. 获取用户行为记录
        List<KbUserBehavior> behaviors = userBehaviorMapper.selectList(
                new LambdaQueryWrapper<KbUserBehavior>()
                        .eq(KbUserBehavior::getUserId, userId)
                        .eq(KbUserBehavior::getKbId, kbId));

        // 2. 统计各类行为数量
        Map<String, Object> stats = new HashMap<>();
        stats.put("viewCount", countBehaviors(behaviors, "VIEW"));
        stats.put("searchCount", countBehaviors(behaviors, "SEARCH"));
        stats.put("downloadCount", countBehaviors(behaviors, "DOWNLOAD"));

        return stats;
    }

    /**
     * 计算文档的个性化推荐分数
     *
     * @param docId 文档ID
     * @param stats 用户行为统计
     * @return 推荐分数
     */
    private double calculatePersonalScore(Long docId, Map<String, Object> stats) {
        double score = 0.0;

        // 根据用户行为类型设置权重
        int viewCount = (int) stats.get("viewCount");
        int searchCount = (int) stats.get("searchCount");
        int downloadCount = (int) stats.get("downloadCount");

        // 计算加权分数
        score += viewCount * 0.3;
        score += searchCount * 0.4;
        score += downloadCount * 0.3;

        return score;
    }

    /**
     * 统计指定类型的行为数量
     *
     * @param behaviors 行为列表
     * @param type      行为类型
     * @return 行为数量
     */
    private long countBehaviors(List<KbUserBehavior> behaviors, String type) {
        return behaviors.stream()
                .filter(b -> type.equals(b.getBehaviorType()))
                .count();
    }

    /**
     * 将文档实体转换为搜索结果VO
     *
     * @param document 文档实体
     * @return 搜索结果VO
     */
    private KbSearchVO convertToSearchVO(KbDocument document) {
        KbSearchVO vo = new KbSearchVO();
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
        return vo;
    }
}

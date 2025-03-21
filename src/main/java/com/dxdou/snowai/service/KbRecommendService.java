package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbSearchVO;

import java.util.List;
import java.util.Map;

/**
 * 知识库推荐服务接口
 *
 * @author foreverdxdou
 */
public interface KbRecommendService {

    /**
     * 获取个性化推荐
     *
     * @param userId 用户ID
     * @param kbId   知识库ID
     * @param page   分页参数
     * @return 推荐结果
     */
    Page<KbSearchVO> getPersonalRecommendations(Long userId, Long kbId, Page<KbSearchVO> page);

    /**
     * 获取热门推荐
     *
     * @param kbId 知识库ID
     * @param page 分页参数
     * @return 推荐结果
     */
    Page<KbSearchVO> getHotRecommendations(Long kbId, Page<KbSearchVO> page);

    /**
     * 获取相关推荐
     *
     * @param docId 文档ID
     * @param kbId  知识库ID
     * @param page  分页参数
     * @return 推荐结果
     */
    Page<KbSearchVO> getRelatedRecommendations(Long docId, Long kbId, Page<KbSearchVO> page);

    /**
     * 记录用户行为
     *
     * @param userId       用户ID
     * @param docId        文档ID
     * @param behaviorType 行为类型
     */
    void recordUserBehavior(Long userId, Long docId, String behaviorType);

    /**
     * 获取用户行为统计
     *
     * @param userId 用户ID
     * @param kbId   知识库ID
     * @return 行为统计信息
     */
    Map<String, Object> getUserBehaviorStats(Long userId, Long kbId);
}
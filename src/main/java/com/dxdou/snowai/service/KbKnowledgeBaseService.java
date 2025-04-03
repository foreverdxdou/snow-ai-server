package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.KbKnowledgeBase;
import com.dxdou.snowai.domain.vo.KbKnowledgeBaseVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库服务接口
 *
 * @author foreverdxdou
 */
public interface KbKnowledgeBaseService extends IService<KbKnowledgeBase> {

    /**
     * 分页查询知识库列表
     *
     * @param page       分页参数
     * @param name       知识库名称
     * @param creatorId  创建者ID
     * @param status     状态
     * @param categoryId
     * @return 知识库列表
     */
    Page<KbKnowledgeBaseVO> getKnowledgeBasePage(Page<KbKnowledgeBase> page, String name, Long creatorId,
            Integer status, Long categoryId);

    /**
     * 根据ID查询知识库信息
     *
     * @param id 知识库ID
     * @return 知识库信息
     */
    KbKnowledgeBaseVO getKnowledgeBaseById(Long id);

    /**
     * 创建知识库
     *
     * @param name        知识库名称
     * @param description 知识库描述
     * @param creatorId   创建者ID
     * @param categoryId  分类ID
     * @return 知识库信息
     */
    KbKnowledgeBaseVO createKnowledgeBase(String name, String description, Long creatorId, Long categoryId);

    /**
     * 更新知识库
     *
     * @param id          知识库ID
     * @param name        知识库名称
     * @param description 知识库描述
     * @param categoryId  分类ID
     * @return 知识库信息
     */
    KbKnowledgeBaseVO updateKnowledgeBase(Long id, String name, String description, Long categoryId);

    /**
     * 删除知识库
     *
     * @param id 知识库ID
     */
    void deleteKnowledgeBase(Long id);

    /**
     * 更新知识库状态
     *
     * @param id     知识库ID
     * @param status 状态
     */
    void updateKnowledgeBaseStatus(Long id, Integer status);

    /**
     * 分配知识库权限
     *
     * @param kbId           知识库ID
     * @param userIds        用户ID列表
     * @param roleIds        角色ID列表
     * @param permissionType 权限类型
     */
    void assignKnowledgeBasePermissions(Long kbId, List<Long> userIds, List<Long> roleIds, Integer permissionType);

    /**
     * 获取用户可访问的知识库列表
     *
     * @param userId 用户ID
     * @return 知识库列表
     */
    List<KbKnowledgeBaseVO> getUserKnowledgeBases(Long userId);

    /**
     * 检查用户是否有权限访问知识库
     *
     * @param userId         用户ID
     * @param kbId           知识库ID
     * @param permissionType 权限类型
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, Long kbId, Integer permissionType);

    /**
     * 根据状态统计知识库数量
     *
     * @param status 状态
     * @return 知识库数量
     */
    long countByStatus(Integer status);

    /**
     * 统计指定时间之后创建的知识库数量
     *
     * @param time 时间
     * @return 知识库数量
     */
    long countByCreateTimeAfter(LocalDateTime time);

    /**
     * 获取最新的知识库列表
     *
     * @param limit 数量限制
     * @return 知识库列表
     */
    List<KbKnowledgeBase> getLatestKbs(int limit);

    /**
     * 获取知识库增长趋势
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 每日新增数量列表
     */
    List<Long> getKbTrend(LocalDateTime startTime, LocalDateTime endTime);
}
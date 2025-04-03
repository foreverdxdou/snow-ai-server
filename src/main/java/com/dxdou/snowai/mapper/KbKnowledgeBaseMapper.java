package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbKnowledgeBase;
import com.dxdou.snowai.domain.vo.KbKnowledgeBaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbKnowledgeBaseMapper extends BaseMapper<KbKnowledgeBase> {

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
    Page<KbKnowledgeBaseVO> selectKnowledgeBaseList(Page<KbKnowledgeBase> page, @Param("name") String name,
            @Param("creatorId") Long creatorId, @Param("status") Integer status, @Param("categoryId") Long categoryId);

    /**
     * 根据ID查询知识库信息
     *
     * @param id 知识库ID
     * @return 知识库信息
     */
    KbKnowledgeBaseVO selectKnowledgeBaseById(@Param("id") Long id);

    /**
     * 查询用户可访问的知识库列表
     *
     * @param userId 用户ID
     * @return 知识库列表
     */
    List<KbKnowledgeBaseVO> selectUserKnowledgeBases(@Param("userId") Long userId);

    /**
     * 查询知识库的文档数量
     *
     * @param kbId 知识库ID
     * @return 文档数量
     */
    Integer selectDocumentCount(@Param("kbId") Long kbId);

    /**
     * 根据状态统计知识库数量
     *
     * @param status 状态
     * @return 知识库数量
     */
    long countByStatus(@Param("status") Integer status);

    /**
     * 统计指定时间之后创建的知识库数量
     *
     * @param time 时间
     * @return 知识库数量
     */
    long countByCreateTimeAfter(@Param("time") LocalDateTime time);

    /**
     * 获取最新的知识库列表
     *
     * @param limit 数量限制
     * @return 知识库列表
     */
    List<KbKnowledgeBase> selectLatestKbs(@Param("limit") int limit);

    /**
     * 获取知识库增长趋势
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 每日新增数量列表
     */
    List<Long> selectKbTrend(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
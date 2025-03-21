package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbKnowledgeBase;
import com.dxdou.snowai.domain.vo.KbKnowledgeBaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * @param page      分页参数
     * @param name      知识库名称
     * @param creatorId 创建者ID
     * @param status    状态
     * @return 知识库列表
     */
    Page<KbKnowledgeBaseVO> selectKnowledgeBaseList(Page<KbKnowledgeBase> page, @Param("name") String name,
            @Param("creatorId") Long creatorId, @Param("status") Integer status);

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
}
package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbTag;
import com.dxdou.snowai.domain.vo.KbTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签Mapper接口
 *
 * @author foreverdxdou
 */
public interface KbTagMapper extends BaseMapper<KbTag> {

    /**
     * 分页查询标签列表
     *
     * @param page      分页参数
     * @param name      标签名称
     * @param creatorId 创建者ID
     * @param status    状态
     * @return 标签列表
     */
    Page<KbTagVO> selectTagList(Page<KbTag> page, @Param("name") String name, @Param("creatorId") Long creatorId,
            @Param("status") Integer status);

    /**
     * 根据ID查询标签
     *
     * @param id 标签ID
     * @return 标签信息
     */
    KbTagVO selectTagById(@Param("id") Long id);

    /**
     * 查询文档的标签列表
     *
     * @param documentId 文档ID
     * @return 标签列表
     */
    List<KbTagVO> selectByDocumentId(@Param("documentId") Long documentId);
}
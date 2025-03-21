package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.KbDocumentTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档标签关联Mapper接口
 *
 * @author foreverdxdou
 */
public interface KbDocumentTagMapper extends BaseMapper<KbDocumentTag> {

    /**
     * 批量插入文档标签关联
     *
     * @param documentTags 文档标签关联列表
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<KbDocumentTag> documentTags);

    /**
     * 根据文档ID删除标签关联
     *
     * @param documentId 文档ID
     * @return 影响行数
     */
    int deleteByDocumentId(@Param("documentId") Long documentId);
}
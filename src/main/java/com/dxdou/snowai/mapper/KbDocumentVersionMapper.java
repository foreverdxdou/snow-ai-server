package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbDocumentVersion;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档版本Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbDocumentVersionMapper extends BaseMapper<KbDocumentVersion> {

    /**
     * 分页查询文档版本列表
     *
     * @param page       分页参数
     * @param documentId 文档ID
     * @return 版本列表
     */
    Page<KbDocumentVersionVO> selectVersionList(Page<KbDocumentVersion> page, @Param("documentId") Long documentId);

    /**
     * 根据ID查询版本信息
     *
     * @param id 版本ID
     * @return 版本信息
     */
    KbDocumentVersionVO selectVersionById(@Param("id") Long id);

    /**
     * 查询文档的最新版本
     *
     * @param documentId 文档ID
     * @return 版本信息
     */
    KbDocumentVersionVO selectLatestVersion(@Param("documentId") Long documentId);

    /**
     * 查询文档的版本历史
     *
     * @param documentId 文档ID
     * @return 版本列表
     */
    List<KbDocumentVersionVO> selectVersionHistory(@Param("documentId") Long documentId);
}
package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 文档Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbDocumentMapper extends BaseMapper<KbDocument> {

    /**
     * 分页查询文档列表
     *
     * @param page       分页参数
     * @param title      文档标题
     * @param kbId       知识库ID
     * @param categoryId 分类ID
     * @param creatorId  创建者ID
     * @param status     状态
     * @return 文档列表
     */
    IPage<KbDocumentVO> selectDocumentList(Page<KbDocument> page, @Param("title") String title,
            @Param("kbId") Long kbId, @Param("categoryId") Long categoryId,
            @Param("creatorId") Long creatorId, @Param("status") Integer status);

    /**
     * 根据ID查询文档信息
     *
     * @param id 文档ID
     * @return 文档信息
     */
    KbDocumentVO selectDocumentById(@Param("id") Long id);

    /**
     * 查询文档的标签列表
     *
     * @param documentId 文档ID
     * @return 标签ID列表
     */
    List<Long> selectDocumentTagIds(@Param("documentId") Long documentId);

    /**
     * 批量插入文档标签关联
     *
     * @param documentId 文档ID
     * @param tagIds     标签ID列表
     */
    void insertDocumentTags(@Param("documentId") Long documentId, @Param("tagIds") List<Long> tagIds);

    /**
     * 删除文档的标签关联
     *
     * @param documentId 文档ID
     */
    void deleteDocumentTags(@Param("documentId") Long documentId);

    /**
     * 更新文档版本号
     *
     * @param id      文档ID
     * @param version 版本号
     */
    void updateDocumentVersion(@Param("id") Long id, @Param("version") Integer version);

    /**
     * 查询文档的标签列表
     *
     * @param documentId 文档ID
     * @return 标签信息列表
     */
    List<KbTagVO> selectDocumentTags(@Param("documentIds") Set<Long> documentIds);

    /**
     * 查询文档的标签列表
     * @param kbIds
     * @return
     */
    List<KbTagVO> selectDocumentTagsByKbIds(@Param("kbIds") Set<Long> kbIds);

    /**
     * 根据解析状态统计文档数量
     *
     * @param status 解析状态
     * @return 文档数量
     */
    long countByParseStatus(@Param("status") Integer status);

    /**
     * 统计指定时间之后创建的文档数量
     *
     * @param time 时间
     * @return 文档数量
     */
    long countByCreateTimeAfter(@Param("time") LocalDateTime time);

    /**
     * 获取最新的文档列表
     *
     * @param limit 数量限制
     * @return 文档列表
     */
    List<KbDocument> selectLatestDocs(@Param("limit") int limit);

    /**
     * 获取文档增长趋势
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 每日新增数量列表
     */
    List<Long> selectDocTrend(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.dto.KbDocumentDTO;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库文档服务接口
 *
 * @author foreverdxdou
 */
public interface KbDocumentService extends IService<KbDocument> {

    /**
     * 上传文档
     *
     * @param file      文件
     * @param kbId      知识库ID
     * @param creatorId 创建者ID
     * @param tagIds
     * @return 文档信息
     */
    KbDocumentVO uploadDocument(MultipartFile file, Long kbId, Long creatorId, List<Long> tagIds);

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
    IPage<KbDocumentVO> getDocumentPage(Page<KbDocument> page, String title, Long kbId, Long categoryId, Long creatorId,
            Integer status);

    /**
     * 获取文档详情
     *
     * @param id 文档ID
     * @return 文档详情
     */
    KbDocumentVO getDocumentById(Long id);

    /**
     * 更新文档
     *
     * @param id  文档ID
     * @param dto 文档信息
     * @return 文档详情
     */
    KbDocumentVO updateDocument(Long id, KbDocumentDTO dto);

    /**
     * 删除文档
     *
     * @param id 文档ID
     */
    void deleteDocument(Long id);

    /**
     * 更新文档状态
     *
     * @param id     文档ID
     * @param status 状态
     */
    void updateDocumentStatus(Long id, Integer status);

    /**
     * 回滚文档版本
     *
     * @param id        文档ID
     * @param versionId 版本ID
     * @return 文档信息
     */
    KbDocumentVO rollbackVersion(Long id, Long versionId);

    /**
     * 获取文档版本历史
     *
     * @param documentId 文档ID
     * @return 版本历史列表
     */
    List<KbDocumentVersionVO> getVersionHistory(Long documentId);

    /**
     * 获取文档标签列表
     *
     * @param documentId 文档ID
     * @return 标签列表
     */
    List<KbTagVO> getDocumentTags(Long documentId);

    /**
     * 更新文档标签
     *
     * @param documentId 文档ID
     * @param tagIds     标签ID列表
     */
    void updateDocumentTags(Long documentId, List<Long> tagIds);

    /**
     * 根据解析状态统计文档数量
     *
     * @param status 解析状态
     * @return 文档数量
     */
    long countByParseStatus(Integer status);

    /**
     * 统计指定时间之后创建的文档数量
     *
     * @param time 时间
     * @return 文档数量
     */
    long countByCreateTimeAfter(LocalDateTime time);

    /**
     * 获取最新的文档列表
     *
     * @param limit 数量限制
     * @return 文档列表
     */
    List<KbDocument> getLatestDocs(int limit);

    /**
     * 获取文档增长趋势
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 每日新增数量列表
     */
    List<Long> getDocTrend(LocalDateTime startTime, LocalDateTime endTime);
}
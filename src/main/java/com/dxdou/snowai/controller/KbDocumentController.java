package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.KbDocumentDTO;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 知识库文档控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "文档管理", description = "知识库文档相关接口")
@RestController
@RequestMapping("/api/v1/kb/document")
@RequiredArgsConstructor
public class KbDocumentController {

    private final KbDocumentService documentService;
    private final AuthService authService;

    /**
     * 上传文档
     *
     * @param file 文件
     * @param kbId 知识库ID
     * @return 文档信息
     */
    @Operation(summary = "上传文档")
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('kb:document:add')")
    public R<KbDocumentVO> upload(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "知识库ID") @RequestParam("kbId") Long kbId,
            @Parameter(description = "标签ID列表") @RequestParam(required = false) List<Long> tagIds) {
        Long creatorId = authService.getCurrentUser().getId();
        KbDocumentVO documentVO = documentService.uploadDocument(file, kbId, creatorId, tagIds);
        documentService.processDocument(documentVO.getId());
        return R.ok(documentVO);
    }

    /**
     * 获取文档解析状态
     *
     * @param id 文档ID
     * @return 解析状态
     */
    @Operation(summary = "获取文档解析状态")
    @GetMapping("/{id}/parse-status")
    @PreAuthorize("hasAuthority('kb:document:view')")
    public R<KbDocumentVO> getParseStatus(@Parameter(description = "文档ID") @PathVariable Long id) {
        return R.ok(documentService.getDocumentById(id));
    }

    @PreAuthorize("hasAuthority('kb:document:list')")
    @Operation(summary = "分页查询文档列表")
    @GetMapping("/page")
    public R<IPage<KbDocumentVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "标题") @RequestParam(required = false) String title,
            @Parameter(description = "知识库ID") @RequestParam(required = false) Long kbId,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "创建者ID") @RequestParam(required = false) Long creatorId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<KbDocument> page = new Page<>(current, size);
        return R.ok(documentService.getDocumentPage(page, title, kbId, categoryId, creatorId, status));
    }

    /**
     * 获取文档详情
     *
     * @param id 文档ID
     * @return 文档详情
     */
    @Operation(summary = "获取文档详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:document:view')")
    public R<KbDocumentVO> getById(@Parameter(description = "文档ID") @PathVariable Long id) {
        return R.ok(documentService.getDocumentById(id));
    }

    /**
     * 更新文档
     *
     * @param id  文档ID
     * @param dto 文档信息
     * @return 文档详情
     */
    @Operation(summary = "更新文档")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:document:edit')")
    public R<KbDocumentVO> update(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Valid @RequestBody KbDocumentDTO dto) {
        KbDocumentVO documentVO = documentService.updateDocument(id, dto);
        documentService.processDocument(documentVO.getId());
        return R.ok(documentVO);
    }

    @Operation(summary = "解析文档")
    @PostMapping("/{id}/parse")
    @PreAuthorize("hasAuthority('kb:document:edit')")
    public R<Void> parse(
            @Parameter(description = "文档ID") @PathVariable Long id) {
        documentService.processDocument(id);
        return R.ok(null);
    }

    /**
     * 删除文档
     *
     * @param id 文档ID
     * @return 操作结果
     */
    @Operation(summary = "删除文档")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:document:delete')")
    public R<Void> delete(@Parameter(description = "文档ID") @PathVariable Long id) {
        documentService.deleteDocument(id);
        return R.ok(null);
    }

    /**
     * 批量删除文档
     *
     * @param ids 文档ID
     * @return 操作结果
     */
    @Operation(summary = "批量删除文档")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('kb:document:delete')")
    public R<Void> deleteBatch(@Parameter(description = "文档ID") @RequestBody List<Long> ids) {
        documentService.removeBatchByIds(ids);
        return R.ok(null);
    }

    @Operation(summary = "更新文档状态")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        documentService.updateDocumentStatus(id, status);
        return R.ok(null);
    }

    @Operation(summary = "回滚版本")
    @PutMapping("/{id}/rollback/{versionId}")
    public R<KbDocumentVO> rollback(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Parameter(description = "版本ID") @PathVariable Long versionId) {
        return R.ok(documentService.rollbackVersion(id, versionId));
    }

    @Operation(summary = "获取版本历史")
    @GetMapping("/{id}/versions")
    public R<List<KbDocumentVersionVO>> getVersionHistory(
            @Parameter(description = "文档ID") @PathVariable Long id) {
        return R.ok(documentService.getVersionHistory(id));
    }

    @Operation(summary = "获取文档标签")
    @GetMapping("/{id}/tags")
    public R<List<KbTagVO>> getDocumentTags(
            @Parameter(description = "文档ID") @PathVariable Long id) {
        return R.ok(documentService.getDocumentTags(id));
    }

    @Operation(summary = "更新文档标签")
    @PutMapping("/{id}/tags")
    public R<Void> updateDocumentTags(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @Parameter(description = "标签ID列表") @RequestParam List<Long> tagIds) {
        documentService.updateDocumentTags(id, tagIds);
        return R.ok(null);
    }
}
package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.service.KbDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "文档管理")
@RestController
@RequestMapping("/api/v1/kb/document")
@RequiredArgsConstructor
public class KbDocumentController {

    private final KbDocumentService documentService;

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

    @Operation(summary = "获取文档详情")
    @GetMapping("/{id}")
    public R<KbDocumentVO> getById(@Parameter(description = "文档ID") @PathVariable Long id) {
        return R.ok(documentService.getDocumentById(id));
    }

    @Operation(summary = "上传文档")
    @PostMapping("/upload")
    public R<KbDocumentVO> upload(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "标题") @RequestParam String title,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "标签ID列表") @RequestParam(required = false) List<Long> tagIds) {
        return R.ok(documentService.uploadDocument(file, title, kbId, categoryId, tagIds));
    }

    @Operation(summary = "更新文档")
    @PutMapping("/{id}")
    public R<KbDocumentVO> update(
            @Parameter(description = "文档ID") @PathVariable Long id,
            @RequestBody KbDocument document) {
        return R.ok(documentService.updateDocument(id, document.getTitle(), document.getContent(), document.getCategoryId(), document.getTagIds()));
    }

    @Operation(summary = "删除文档")
    @DeleteMapping("/{id}")
    public R<Void> delete(@Parameter(description = "文档ID") @PathVariable Long id) {
        documentService.deleteDocument(id);
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
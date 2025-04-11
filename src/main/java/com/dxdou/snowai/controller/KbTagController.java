package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.KbTag;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("/api/v1/kb/tag")
@RequiredArgsConstructor
public class KbTagController {

    private final KbTagService tagService;
    private final AuthService authService;

    @Operation(summary = "分页查询标签列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('kb:tag:view')")
    public R<Page<KbTagVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "标签名称") @RequestParam(required = false) String name,
            @Parameter(description = "创建者ID") @RequestParam(required = false) Long creatorId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Page<KbTag> page = new Page<>(current, size);
        return R.ok(tagService.getTagPage(page, name, creatorId, status));
    }

    @Operation(summary = "获取标签详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:tag:view')")
    public R<KbTagVO> getById(@Parameter(description = "标签ID") @PathVariable Long id) {
        return R.ok(tagService.getTagById(id));
    }

    @Operation(summary = "创建标签")
    @PostMapping
    @PreAuthorize("hasAuthority('kb:tag:add')")
    public R<KbTagVO> create(@RequestBody KbTag tag) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(tagService.createTag(tag.getName(), tag.getDescription(), creatorId));
    }

    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:tag:edit')")
    public R<KbTagVO> update(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @RequestBody KbTag tag) {
        return R.ok(tagService.updateTag(id, tag.getName(), tag.getDescription()));
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:tag:delete')")
    public R<Void> delete(@Parameter(description = "标签ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return R.ok(null);
    }

    @Operation(summary = "批量删除标签")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('kb:tag:delete')")
    public R<Void> deleteBatch(@Parameter(description = "标签ID") @RequestBody List<Long> ids) {
        tagService.removeBatchByIds(ids);
        return R.ok(null);
    }

    @Operation(summary = "更新标签状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('kb:tag:edit')")
    public R<Void> updateStatus(
            @Parameter(description = "标签ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        tagService.updateTagStatus(id, status);
        return R.ok(null);
    }
}
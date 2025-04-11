package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.KbCategoryCreateDTO;
import com.dxdou.snowai.domain.dto.KbCategoryUpdateDTO;
import com.dxdou.snowai.domain.entity.KbCategory;
import com.dxdou.snowai.domain.vo.KbCategoryVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库分类控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库分类管理", description = "知识库分类相关接口")
@RestController
@RequestMapping("/api/v1/kb/category")
@RequiredArgsConstructor
public class KbCategoryController {

    private final KbCategoryService categoryService;
    private final AuthService authService;

    /**
     * 分页查询分类列表
     *
     * @param page   分页参数
     * @param name   分类名称
     * @param status 状态
     * @return 分类列表
     */
    @Operation(summary = "分页查询分类列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('kb:category:list')")
    public R<Page<KbCategoryVO>> list(
            @Parameter(description = "分页参数") Page<KbCategory> page,
            @Parameter(description = "分类名称") @RequestParam(required = false) String name,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return R.ok(categoryService.getCategoryPage(page, name, status));
    }

    /**
     * 获取分类详情
     *
     * @param id 分类ID
     * @return 分类信息
     */
    @Operation(summary = "获取分类详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:category:view')")
    public R<KbCategoryVO> getById(@Parameter(description = "分类ID") @PathVariable Long id) {
        return R.ok(categoryService.getCategoryById(id));
    }

    /**
     * 创建分类
     *
     * @param dto 分类信息
     * @return 分类信息
     */
    @Operation(summary = "创建分类")
    @PostMapping
    @PreAuthorize("hasAuthority('kb:category:add')")
    public R<KbCategoryVO> create(@RequestBody KbCategoryCreateDTO dto) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(categoryService.createCategory(dto.getKbId(), dto.getName(), dto.getDescription(), dto.getParentId(), dto.getSort(), creatorId));
    }

    /**
     * 更新分类
     *
     * @param id  分类ID
     * @param dto 分类信息
     * @return 分类信息
     */
    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:category:edit')")
    public R<KbCategoryVO> update(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @RequestBody KbCategoryUpdateDTO dto) {
        return R.ok(categoryService.updateCategory(id, dto.getName(), dto.getDescription(), dto.getParentId(), dto.getSort()));
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 操作结果
     */
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:category:delete')")
    public R<Void> delete(@Parameter(description = "分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return R.ok(null);
    }

    /**
     * 批量删除分类
     *
     * @param ids 分类ID
     * @return 操作结果
     */
    @Operation(summary = "批量删除分类")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('kb:category:delete')")
    public R<Void> deleteBatch(@Parameter(description = "分类ID") @RequestBody List<Long> ids) {
        categoryService.removeBatchByIds(ids);
        return R.ok(null);
    }

    /**
     * 更新分类状态
     *
     * @param id     分类ID
     * @param status 状态
     * @return 操作结果
     */
    @Operation(summary = "更新分类状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('kb:category:edit')")
    public R<Void> updateStatus(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        categoryService.updateCategoryStatus(id, status);
        return R.ok(null);
    }

    /**
     * 获取知识库的分类树
     *
     * @return 分类树
     */
    @Operation(summary = "获取知识库的分类树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('kb:category:view')")
    public R<List<KbCategoryVO>> getCategoryTree() {
        return R.ok(categoryService.getCategoryTree());
    }

    /**
     * 移动分类
     *
     * @param id       分类ID
     * @param parentId 目标父分类ID
     * @param sort     目标排序号
     * @return 操作结果
     */
    @Operation(summary = "移动分类")
    @PutMapping("/{id}/move")
    @PreAuthorize("hasAuthority('kb:category:edit')")
    public R<Void> moveCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "目标父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "目标排序号") @RequestParam(required = false) Integer sort) {
        categoryService.moveCategory(id, parentId, sort);
        return R.ok(null);
    }
}
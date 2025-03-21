package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.Result;
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
@RequestMapping("/api/kb/category")
@RequiredArgsConstructor
public class KbCategoryController {

    private final KbCategoryService categoryService;
    private final AuthService authService;

    /**
     * 分页查询分类列表
     *
     * @param page   分页参数
     * @param kbId   知识库ID
     * @param name   分类名称
     * @param status 状态
     * @return 分类列表
     */
    @Operation(summary = "分页查询分类列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('kb:category:list')")
    public Result<Page<KbCategoryVO>> list(
            @Parameter(description = "分页参数") Page<KbCategory> page,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "分类名称") @RequestParam(required = false) String name,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return Result.success(categoryService.getCategoryPage(page, kbId, name, status));
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
    public Result<KbCategoryVO> getById(@Parameter(description = "分类ID") @PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    /**
     * 创建分类
     *
     * @param kbId        知识库ID
     * @param name        分类名称
     * @param description 分类描述
     * @param parentId    父分类ID
     * @param sort        排序号
     * @return 分类信息
     */
    @Operation(summary = "创建分类")
    @PostMapping
    @PreAuthorize("hasAuthority('kb:category:add')")
    public Result<KbCategoryVO> create(
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "分类名称") @RequestParam String name,
            @Parameter(description = "分类描述") @RequestParam(required = false) String description,
            @Parameter(description = "父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "排序号") @RequestParam(required = false) Integer sort) {
        Long creatorId = authService.getCurrentUser().getId();
        return Result.success(categoryService.createCategory(kbId, name, description, parentId, sort, creatorId));
    }

    /**
     * 更新分类
     *
     * @param id          分类ID
     * @param name        分类名称
     * @param description 分类描述
     * @param parentId    父分类ID
     * @param sort        排序号
     * @return 分类信息
     */
    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:category:edit')")
    public Result<KbCategoryVO> update(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "分类名称") @RequestParam String name,
            @Parameter(description = "分类描述") @RequestParam(required = false) String description,
            @Parameter(description = "父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "排序号") @RequestParam(required = false) Integer sort) {
        return Result.success(categoryService.updateCategory(id, name, description, parentId, sort));
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
    public Result<Void> delete(@Parameter(description = "分类ID") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
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
    public Result<Void> updateStatus(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        categoryService.updateCategoryStatus(id, status);
        return Result.success();
    }

    /**
     * 获取知识库的分类树
     *
     * @param kbId 知识库ID
     * @return 分类树
     */
    @Operation(summary = "获取知识库的分类树")
    @GetMapping("/tree/{kbId}")
    public Result<List<KbCategoryVO>> getCategoryTree(@Parameter(description = "知识库ID") @PathVariable Long kbId) {
        return Result.success(categoryService.getCategoryTree(kbId));
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
    public Result<Void> moveCategory(
            @Parameter(description = "分类ID") @PathVariable Long id,
            @Parameter(description = "目标父分类ID") @RequestParam(required = false) Long parentId,
            @Parameter(description = "目标排序号") @RequestParam(required = false) Integer sort) {
        categoryService.moveCategory(id, parentId, sort);
        return Result.success();
    }
}
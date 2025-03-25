package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.KnowledgeBaseAssignDTO;
import com.dxdou.snowai.domain.entity.KbKnowledgeBase;
import com.dxdou.snowai.domain.dto.KnowledgeBaseDTO;
import com.dxdou.snowai.domain.vo.KbKnowledgeBaseVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbKnowledgeBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库管理", description = "知识库相关接口")
@RestController
@RequestMapping("/api/v1/kb")
@RequiredArgsConstructor
public class KbKnowledgeBaseController {

    private final KbKnowledgeBaseService knowledgeBaseService;
    private final AuthService authService;

    /**
     * 分页查询知识库列表
     *
     * @param page   分页参数
     * @param name   知识库名称
     * @param status 状态
     * @return 知识库列表
     */
    @Operation(summary = "分页查询知识库列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('kb:list')")
    public R<Page<KbKnowledgeBaseVO>> list(
            @Parameter(description = "分页参数") Page<KbKnowledgeBase> page,
            @Parameter(description = "知识库名称") @RequestParam(required = false) String name,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(knowledgeBaseService.getKnowledgeBasePage(page, name, creatorId, status));
    }

    /**
     * 获取知识库详情
     *
     * @param id 知识库ID
     * @return 知识库信息
     */
    @Operation(summary = "获取知识库详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:view')")
    public R<KbKnowledgeBaseVO> getById(@Parameter(description = "知识库ID") @PathVariable Long id) {
        return R.ok(knowledgeBaseService.getKnowledgeBaseById(id));
    }

    /**
     * 创建知识库
     *
     * @param dto 知识库信息
     * @return 知识库信息
     */
    @Operation(summary = "创建知识库")
    @PostMapping
    @PreAuthorize("hasAuthority('kb:add')")
    public R<KbKnowledgeBaseVO> create(@RequestBody KnowledgeBaseDTO dto) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(knowledgeBaseService.createKnowledgeBase(dto.getName(), dto.getDescription(), creatorId, dto.getCategoryId()));
    }

    /**
     * 更新知识库
     *
     * @param id  知识库ID
     * @param dto 知识库信息
     * @return 知识库信息
     */
    @Operation(summary = "更新知识库")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:edit')")
    public R<KbKnowledgeBaseVO> update(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @RequestBody KnowledgeBaseDTO dto) {
        return R.ok(knowledgeBaseService.updateKnowledgeBase(id, dto.getName(), dto.getDescription(), dto.getCategoryId()));
    }

    /**
     * 删除知识库
     *
     * @param id 知识库ID
     * @return 操作结果
     */
    @Operation(summary = "删除知识库")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('kb:delete')")
    public R<Void> delete(@Parameter(description = "知识库ID") @PathVariable Long id) {
        knowledgeBaseService.deleteKnowledgeBase(id);
        return R.ok(null);
    }

    /**
     * 更新知识库状态
     *
     * @param id     知识库ID
     * @param status 状态
     * @return 操作结果
     */
    @Operation(summary = "更新知识库状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('kb:edit')")
    public R<Void> updateStatus(
            @Parameter(description = "知识库ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        knowledgeBaseService.updateKnowledgeBaseStatus(id, status);
        return R.ok(null);
    }

    /**
     * 分配知识库权限
     *
     * @param kbId 知识库ID
     * @param dto  权限信息
     * @return 操作结果
     */
    @Operation(summary = "分配知识库权限")
    @PostMapping("/assignPermissions/{kbId}")
    @PreAuthorize("hasAuthority('kb:permission')")
    public R<Void> assignPermissions(
            @Parameter(description = "知识库ID") @PathVariable Long kbId,
            @RequestBody KnowledgeBaseAssignDTO dto) {
        knowledgeBaseService.assignKnowledgeBasePermissions(kbId, dto.getUserIds(), dto.getRoleIds(), dto.getPermissionType());
        return R.ok(null);
    }

    /**
     * 获取用户可访问的知识库列表
     *
     * @return 知识库列表
     */
    @Operation(summary = "获取用户可访问的知识库列表")
    @GetMapping("/user/list")
    public R<List<KbKnowledgeBaseVO>> getUserKnowledgeBases() {
        Long userId = authService.getCurrentUser().getId();
        return R.ok(knowledgeBaseService.getUserKnowledgeBases(userId));
    }
}
package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.EmbeddingConfigDTO;
import com.dxdou.snowai.domain.entity.EmbeddingConfig;
import com.dxdou.snowai.domain.vo.EmbeddingConfigVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.EmbeddingConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Embedding模型配置控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "Embedding模型配置管理", description = "Embedding模型配置相关接口")
@RestController
@RequestMapping("/api/v1/embedding/config")
@RequiredArgsConstructor
public class EmbeddingConfigController {

    private final EmbeddingConfigService embeddingConfigService;
    private final AuthService authService;

    /**
     * 分页查询Embedding模型配置列表
     *
     * @param page    分页参数
     * @param name    模型名称
     * @param enabled 是否启用
     * @return 配置列表
     */
    @Operation(summary = "分页查询Embedding模型配置列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('embedding:list')")
    public R<Page<EmbeddingConfigVO>> list(
            @Parameter(description = "分页参数") Page<EmbeddingConfig> page,
            @Parameter(description = "模型名称") @RequestParam(required = false) String name,
            @Parameter(description = "是否启用") @RequestParam(required = false) Integer enabled) {
        return R.ok(embeddingConfigService.getEmbeddingConfigPage(page, name, enabled));
    }

    /**
     * 获取Embedding模型配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    @Operation(summary = "获取Embedding模型配置详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('embedding:view')")
    public R<EmbeddingConfigVO> getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        return R.ok(embeddingConfigService.getEmbeddingConfigById(id));
    }

    /**
     * 创建Embedding模型配置
     *
     * @param dto 配置信息
     * @return 配置详情
     */
    @Operation(summary = "创建Embedding模型配置")
    @PostMapping
    @PreAuthorize("hasAuthority('embedding:add')")
    public R<EmbeddingConfigVO> create(@Validated @RequestBody EmbeddingConfigDTO dto) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(embeddingConfigService.createEmbeddingConfig(dto, creatorId));
    }

    /**
     * 更新Embedding模型配置
     *
     * @param id  配置ID
     * @param dto 配置信息
     * @return 配置详情
     */
    @Operation(summary = "更新Embedding模型配置")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('embedding:edit')")
    public R<EmbeddingConfigVO> update(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @Validated @RequestBody EmbeddingConfigDTO dto) {
        Long updaterId = authService.getCurrentUser().getId();
        return R.ok(embeddingConfigService.updateEmbeddingConfig(id, dto, updaterId));
    }

    /**
     * 删除Embedding模型配置
     *
     * @param id 配置ID
     * @return 操作结果
     */
    @Operation(summary = "删除Embedding模型配置")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('embedding:delete')")
    public R<Void> delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        embeddingConfigService.deleteEmbeddingConfig(id);
        return R.ok(null);
    }

    @Operation(summary = "修改Embedding模型配置状态")
    @PutMapping("/{id}/status")
    public R<EmbeddingConfigVO> updateEnabledStatus(@Parameter(description = "配置ID") @PathVariable Long id,
                                                    @Parameter(description = "状态") @RequestParam Integer status) {
        return R.ok(embeddingConfigService.updateEnabledStatus(id, 1 == status));
    }

    @Operation(summary = "获取启用的Embedding模型配置")
    @GetMapping("/enabled")
    public R<EmbeddingConfigVO> getEnabledConfig() {
        return R.ok(embeddingConfigService.getEnabledEmbeddingConfig());
    }
}
package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.SystemConfigDTO;
import com.dxdou.snowai.domain.entity.SystemConfig;
import com.dxdou.snowai.domain.vo.SystemConfigVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "系统配置管理", description = "系统配置相关接口")
@RestController
@RequestMapping("/api/v1/system/config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;
    private final AuthService authService;

    /**
     * 分页查询系统配置列表
     *
     * @param page       分页参数
     * @param configKey  配置键
     * @param configType 配置类型
     * @return 配置列表
     */
    @Operation(summary = "分页查询系统配置列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:config:list')")
    public R<Page<SystemConfigVO>> list(
            @Parameter(description = "分页参数") Page<SystemConfig> page,
            @Parameter(description = "配置键") @RequestParam(required = false) String configKey,
            @Parameter(description = "配置类型") @RequestParam(required = false) String configType) {
        return R.ok(systemConfigService.getSystemConfigPage(page, configKey, configType));
    }

    /**
     * 获取系统配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    @Operation(summary = "获取系统配置详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:config:view')")
    public R<SystemConfigVO> getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        return R.ok(systemConfigService.getSystemConfigById(id));
    }

    /**
     * 创建系统配置
     *
     * @param dto 配置信息
     * @return 配置详情
     */
    @Operation(summary = "创建系统配置")
    @PostMapping
    @PreAuthorize("hasAuthority('system:config:add')")
    public R<SystemConfigVO> create(@Validated @RequestBody SystemConfigDTO dto) {
        Long creatorId = authService.getCurrentUser().getId();
        return R.ok(systemConfigService.createSystemConfig(dto, creatorId));
    }

    /**
     * 更新系统配置
     *
     * @param id  配置ID
     * @param dto 配置信息
     * @return 配置详情
     */
    @Operation(summary = "更新系统配置")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('system:config:edit')")
    public R<SystemConfigVO> update(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @Validated @RequestBody SystemConfigDTO dto) {
        Long updaterId = authService.getCurrentUser().getId();
        return R.ok(systemConfigService.updateSystemConfig(id, dto, updaterId));
    }

    /**
     * 删除系统配置
     *
     * @param id 配置ID
     * @return 操作结果
     */
    @Operation(summary = "删除系统配置")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:config:delete')")
    public R<Void> delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        systemConfigService.deleteSystemConfig(id);
        return R.ok(null);
    }

    /**
     * 获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    @Operation(summary = "获取配置值")
    @GetMapping("/value/{configKey}")
    public R<String> getConfigValue(@Parameter(description = "配置键") @PathVariable String configKey) {
        return R.ok(systemConfigService.getConfigValue(configKey));
    }
}
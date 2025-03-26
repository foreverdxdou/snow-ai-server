package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.LlmConfig;
import com.dxdou.snowai.service.LlmConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大模型配置控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "大模型配置")
@RestController
@RequestMapping("/api/v1/llm/config")
@RequiredArgsConstructor
public class LlmConfigController {

    private final LlmConfigService llmConfigService;

    @Operation(summary = "获取所有大模型配置")
    @GetMapping("/list")
    public R<List<LlmConfig>> list() {
        return R.ok(llmConfigService.list(Wrappers.lambdaQuery(LlmConfig.class).orderByDesc(LlmConfig::getCreateTime)));
    }

    @Operation(summary = "获取指定大模型配置")
    @GetMapping("/{id}")
    public R<LlmConfig> getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        return R.ok(llmConfigService.getById(id));
    }

    @Operation(summary = "新增大模型配置")
    @PostMapping
    public R<Boolean> save(@RequestBody LlmConfig config) {
        return R.ok(llmConfigService.save(config));
    }

    @Operation(summary = "更新大模型配置")
    @PutMapping
    public R<Boolean> update(@RequestBody LlmConfig config) {
        return R.ok(llmConfigService.updateById(config));
    }

    @Operation(summary = "删除大模型配置")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        return R.ok(llmConfigService.removeById(id));
    }
}
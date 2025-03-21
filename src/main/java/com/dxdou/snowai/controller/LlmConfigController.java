package com.dxdou.snowai.controller;

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
    public List<LlmConfig> list() {
        return llmConfigService.list();
    }

    @Operation(summary = "获取指定大模型配置")
    @GetMapping("/{id}")
    public LlmConfig getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        return llmConfigService.getById(id);
    }

    @Operation(summary = "新增大模型配置")
    @PostMapping
    public boolean save(@RequestBody LlmConfig config) {
        return llmConfigService.save(config);
    }

    @Operation(summary = "更新大模型配置")
    @PutMapping
    public boolean update(@RequestBody LlmConfig config) {
        return llmConfigService.updateById(config);
    }

    @Operation(summary = "删除大模型配置")
    @DeleteMapping("/{id}")
    public boolean delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        return llmConfigService.removeById(id);
    }
}
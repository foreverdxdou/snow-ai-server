package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 大模型配置实体
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "大模型配置VO")
public class LlmConfigVO {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 模型名称
     */
    @Schema(description = "模型名称")
    private String modelName;

    /**
     * 模型编码
     */
    @Schema(description = "模型编码")
    private String modelCode;

    /**
     * 模型提供者
     */
    @Schema(description = "模型提供者 openai,anthropic,google,meta,microsoft,amazon,baidu,alibaba,tencent,zhipu,minimax,moonshot,deepseek,other")
    private String modelProvider;

    /**
     * 模型类型 1: 通用 2: 推理
     */
    @Schema(description = "模型类型 1: 通用 2: 推理")
    private String modelType;

    /**
     * API地址
     */
    @Schema(description = "API地址")
    private String apiUrl;

    private String apiKey;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enabled;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
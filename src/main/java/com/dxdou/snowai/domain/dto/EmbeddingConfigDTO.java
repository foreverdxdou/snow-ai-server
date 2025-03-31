package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Embedding模型配置DTO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "Embedding模型配置DTO")
public class EmbeddingConfigDTO {

    @Schema(description = "模型名称")
    @NotBlank(message = "模型名称不能为空")
    private String name;

    @Schema(description = "模型编码")
    @NotBlank(message = "模型编码不能为空")
    private String modelType;

    @Schema(description = "API密钥")
    @NotBlank(message = "API密钥不能为空")
    private String apiKey;

    @Schema(description = "API基础URL")
    @NotBlank(message = "API基础URL不能为空")
    private String baseUrl;

    @Schema(description = "模型维度")
    @NotNull(message = "模型维度不能为空")
    private Integer dimensions;

    @Schema(description = "是否启用")
    @NotNull(message = "是否启用不能为空")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
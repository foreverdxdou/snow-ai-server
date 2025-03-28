package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 系统配置DTO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统配置DTO")
public class SystemConfigDTO {

    @Schema(description = "配置键")
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    @Schema(description = "配置值")
    @NotBlank(message = "配置值不能为空")
    private String configValue;

    @Schema(description = "配置描述")
    private String description;

    @Schema(description = "配置类型")
    private String configType;
}
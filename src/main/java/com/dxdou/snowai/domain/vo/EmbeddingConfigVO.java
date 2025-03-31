package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Embedding模型配置VO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "Embedding模型配置VO")
public class EmbeddingConfigVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "模型名称")
    private String name;

    @Schema(description = "模型编码")
    private String modelType;

    @Schema(description = "API密钥")
    private String apiKey;

    @Schema(description = "API基础URL")
    private String baseUrl;

    @Schema(description = "模型维度")
    private Integer dimensions;

    @Schema(description = "是否启用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "更新者名称")
    private String updaterName;

    @Schema(description = "备注")
    private String remark;
}
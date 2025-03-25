package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "知识库请求参数")
public class KnowledgeBaseDTO {

    @Schema(description = "知识库名称")
    private String name;

    @Schema(description = "知识库描述")
    private String description;

    @Schema(description = "分类ID")
    private Long categoryId;
}
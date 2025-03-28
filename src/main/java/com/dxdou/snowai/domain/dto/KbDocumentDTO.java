package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 知识库文档DTO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "知识库文档DTO")
public class KbDocumentDTO {

    @Schema(description = "文档ID")
    private Long id;

    @Schema(description = "文档标题")
    @NotBlank(message = "文档标题不能为空")
    private String title;

    @Schema(description = "文档内容")
    @NotBlank(message = "文档内容不能为空")
    private String content;

    @Schema(description = "知识库ID")
    @NotNull(message = "知识库ID不能为空")
    private Long kbId;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "状态")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;
}
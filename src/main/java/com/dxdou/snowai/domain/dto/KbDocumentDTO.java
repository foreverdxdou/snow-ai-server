package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 文档请求参数
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "文档请求参数")
public class KbDocumentDTO {

    @Schema(description = "文档ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "知识库ID")
    private Long kbId;

    @Schema(description = "状态（1：正常，0：禁用）")
    private Integer status;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;
}
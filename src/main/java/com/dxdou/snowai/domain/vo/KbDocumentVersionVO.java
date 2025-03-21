package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档版本VO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "文档版本VO")
public class KbDocumentVersionVO {

    @Schema(description = "版本ID")
    private Long id;

    @Schema(description = "文档ID")
    private Long documentId;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
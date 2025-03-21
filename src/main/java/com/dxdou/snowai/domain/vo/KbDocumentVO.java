package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档VO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "文档VO")
public class KbDocumentVO {

    @Schema(description = "文档ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "知识库ID")
    private Long kbId;

    @Schema(description = "知识库名称")
    private String kbName;

    @Schema(description = "创建者ID")
    private Long creatorId;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "标签列表")
    private List<KbTagVO> tags;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
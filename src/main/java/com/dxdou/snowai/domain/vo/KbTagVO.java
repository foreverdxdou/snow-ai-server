package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签VO
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "标签VO")
public class KbTagVO {

    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "知识库ID")
    private Long kbId;

    @Schema(description = "文档ID")
    private Long documentId;

//    @Schema(description = "标签描述")
//    private String description;
//
//
//    @Schema(description = "知识库名称")
//    private String kbName;
//
//    @Schema(description = "创建者ID")
//    private Long creatorId;
//
//    @Schema(description = "创建者名称")
//    private String creatorName;
//
//    @Schema(description = "状态")
//    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dxdou.snowai.handler.MinioTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_document")
public class KbDocument {

    /**
     * 文档ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件URL
     */
    @TableField(typeHandler = MinioTypeHandler.class)
    private String fileUrl;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 状态（1：正常，0：禁用）
     */
    private Integer status;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 解析状态（0：未解析，1：解析中，2：解析成功，3：解析失败）
     */
    private Integer parseStatus;

    /**
     * 解析失败原因
     */
    private String parseError;
}
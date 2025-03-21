package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档标签关联实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_document_tag")
public class KbDocumentTag {

    /**
     * 关联ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer deleted;
}
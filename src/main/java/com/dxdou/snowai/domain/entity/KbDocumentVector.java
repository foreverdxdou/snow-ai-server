package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dxdou.snowai.handler.VectorTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档向量实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_document_vector")
public class KbDocumentVector {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 内容向量
     */
    @TableField(typeHandler = VectorTypeHandler.class)
    private float[] contentVector;

    /**
     * 分块索引
     */
    private Integer chunkIndex;

    /**
     * 分块内容
     */
    private String chunkContent;

    /**
     * 相似度分数
     */
    private Double similarity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
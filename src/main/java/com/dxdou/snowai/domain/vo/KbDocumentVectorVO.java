package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文档向量VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbDocumentVectorVO {

    /**
     * 向量ID
     */
    private Long id;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 内容向量
     */
    private String contentVector;

    /**
     * 分块索引
     */
    private Integer chunkIndex;

    /**
     * 分块内容
     */
    private String chunkContent;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
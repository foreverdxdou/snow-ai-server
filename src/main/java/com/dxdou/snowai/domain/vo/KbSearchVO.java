package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库搜索结果VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbSearchVO {

    /**
     * 文档ID
     */
    private Long id;

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档内容
     */
    private String content;

    /**
     * 文档摘要
     */
    private String summary;

    /**
     * 关键词
     */
    private String keywords;

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
    private String fileUrl;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 知识库名称
     */
    private String kbName;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 标签列表
     */
    private List<KbTagVO> tags;

    /**
     * 相似度分数
     */
    private Double similarity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private String matchedContent; // 匹配的文档块内容
}
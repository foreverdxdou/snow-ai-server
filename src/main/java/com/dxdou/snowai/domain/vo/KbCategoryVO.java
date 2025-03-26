package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库分类VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbCategoryVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子分类列表
     */
    private List<KbCategoryVO> children;

    /**
     * 文档数量
     */
    private Integer documentCount;
}
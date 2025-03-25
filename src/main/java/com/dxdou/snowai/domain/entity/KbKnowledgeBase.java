package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_knowledge_base")
public class KbKnowledgeBase {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 知识库名称
     */
    private String name;

    /**
     * 知识库描述
     */
    private String description;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 部门ID
     */
    private Long deptId;

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
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer deleted;
}
package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbKnowledgeBaseVO {

    /**
     * 主键ID
     */
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
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 文档数量
     */
    private Integer documentCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 分类数量
     */
    private Integer categoryCount;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 用户权限列表
     */
    private List<KbKnowledgeBasePermissionVO> userPermissions;

    /**
     * 角色权限列表
     */
    private List<KbKnowledgeBasePermissionVO> rolePermissions;
}
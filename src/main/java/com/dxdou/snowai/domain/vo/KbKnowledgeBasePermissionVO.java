package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库权限VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbKnowledgeBasePermissionVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限类型（1：查看，2：编辑，3：管理）
     */
    private Integer permissionType;

    /**
     * 权限类型名称
     */
    private String permissionTypeName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
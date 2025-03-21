package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统权限VO类
 *
 * @author foreverdxdou
 */
@Data
public class SysPermissionVO {

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限类型（1：菜单 2：按钮）
     */
    private Integer type;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（1：正常，0：禁用）
     */
    private Integer status;

    /**
     * 是否有子节点
     */
    private Integer hasChildren;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
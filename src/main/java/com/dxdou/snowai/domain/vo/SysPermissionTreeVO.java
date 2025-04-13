package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统权限树VO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统权限树")
public class SysPermissionTreeVO {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private String id;

    /**
     * 权限ID（用于前端展示）
     */
    @Schema(description = "权限ID（用于前端展示）")
    private String itemId;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID")
    private String parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    private String name;

    /**
     * 权限类型（1：菜单 2：按钮）
     */
    @Schema(description = "权限类型（1：菜单 2：按钮）")
    private Integer type;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    private String permissionCode;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String component;

    /**
     * 图标
     */
    @Schema(description = "图标")
    private String icon;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态（1：正常，0：禁用）
     */
    @Schema(description = "状态（1：正常，0：禁用）")
    private Integer status;

    /**
     * 是否有子节点
     */
    @Schema(description = "是否有子节点")
    private Integer hasChildren;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 子权限列表
     */
    @Schema(description = "子权限列表")
    private List<SysPermissionTreeVO> children;
}
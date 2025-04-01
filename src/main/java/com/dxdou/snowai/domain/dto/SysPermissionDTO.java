package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统权限DTO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统权限DTO")
public class SysPermissionDTO {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long id;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID")
    private Long parentId;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    @Schema(description = "权限名称", required = true)
    private String name;

    /**
     * 权限类型（1：菜单 2：按钮）
     */
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "权限类型：1-菜单，2-按钮", required = true)
    private Integer type;

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100个字符")
    @Schema(description = "权限编码", required = true)
    private String permissionCode;

    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址长度不能超过200个字符")
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 200, message = "组件路径长度不能超过200个字符")
    @Schema(description = "组件路径")
    private String component;

    /**
     * 图标
     */
    @Size(max = 50, message = "图标长度不能超过50个字符")
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
    @Schema(description = "状态：1-正常，0-禁用")
    private Integer status;
}
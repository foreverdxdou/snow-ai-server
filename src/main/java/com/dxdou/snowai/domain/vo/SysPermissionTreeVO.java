package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
     * 子权限列表
     */
    @Schema(description = "子权限列表")
    private List<SysPermissionTreeVO> children;
}
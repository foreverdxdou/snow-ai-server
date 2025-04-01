package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统角色DTO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统角色DTO")
public class SysRoleDTO {

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Schema(description = "角色名称", required = true)
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    @Schema(description = "角色编码", required = true)
    private String roleCode;

    /**
     * 角色描述
     */
    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    @Schema(description = "角色描述")
    private String description;

    /**
     * 状态（1：正常，0：禁用）
     */
    @Schema(description = "角色状态：1-正常，0-禁用")
    private Integer status;

    /**
     * 权限ID列表
     */
    @Schema(description = "角色权限ID列表")
    private Long[] permissionIds;
}
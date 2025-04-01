package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 系统用户DTO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统用户DTO")
public class SysUserDTO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4-20个字符之间")
    @Schema(description = "用户名", required = true)
    private String username;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Schema(description = "用户昵称", required = true)
    private String nickname;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    @Schema(description = "用户手机号")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "用户头像URL")
    private String avatar;

    /**
     * 部门ID
     */
    @Schema(description = "所属部门ID")
    private Long deptId;

    /**
     * 状态（1：正常，0：禁用）
     */
    @Schema(description = "用户状态：1-正常，0-禁用")
    private Integer status;

    /**
     * 角色ID列表
     */
    @Schema(description = "用户角色ID列表")
    private Long[] roleIds;

    @Schema(description = "密码")
    private String password;


}
package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户VO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "系统用户信息")
public class SysUserVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "用户昵称")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 手机号
     */
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
     * 状态（0：禁用，1：正常，2：锁定，3：账户过期，4：密码过期）
     */
    @Schema(description = "用户状态：0-禁用，1-正常，2-锁定，3-账户过期，4-密码过期")
    private Integer status;

    /**
     * 角色名称列表
     */
    @Schema(description = "用户角色名称列表，多个角色用逗号分隔")
    private String roleNames;

    /**
     * 角色编码列表
     */
    @Schema(description = "用户角色编码列表，多个角色用逗号分隔")
    private String roleCodes;

    /**
     * 创建时间
     */
    @Schema(description = "用户创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "用户信息更新时间")
    private LocalDateTime updateTime;
}
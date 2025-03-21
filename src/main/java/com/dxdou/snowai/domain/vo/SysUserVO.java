package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户VO类
 *
 * @author foreverdxdou
 */
@Data
public class SysUserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 状态（1：正常，0：禁用）
     */
    private Integer status;

    /**
     * 角色名称列表
     */
    private String roleNames;

    /**
     * 角色编码列表
     */
    private String roleCodes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
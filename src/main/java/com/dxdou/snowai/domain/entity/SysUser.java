package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统用户实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("sys_user")
public class SysUser {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

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
     * 逻辑删除标识（1：已删除，0：未删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
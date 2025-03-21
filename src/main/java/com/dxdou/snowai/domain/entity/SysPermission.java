package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统权限实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("sys_permission")
public class SysPermission {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
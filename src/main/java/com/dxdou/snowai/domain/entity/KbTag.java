package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_tag")
public class KbTag {

    /**
     * 标签ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 状态（1：正常，0：禁用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer deleted;
}
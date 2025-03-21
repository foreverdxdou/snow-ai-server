package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI模型配置实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("sys_ai_model")
public class SysAiModel {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型类型（1:推理模型 2:普通模型）
     */
    private Integer modelType;

    /**
     * API URL
     */
    private String apiUrl;

    /**
     * API Key
     */
    private String apiKey;

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
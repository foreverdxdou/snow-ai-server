package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 大模型配置实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("llm_config")
public class LlmConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * API地址
     */
    private String apiUrl;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 模型编码
     */
    private String modelCode;

    /**
     * 模型提供者 openai,anthropic,google,meta,microsoft,amazon,baidu,alibaba,tencent,zhipu,minimax,moonshot,deepseek,other
     */
    private String modelProvider;

    /**
     * 模型类型 GENERAL: 通用 REASONING: 推理
     */
    private String modelType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI模型Token使用情况实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("llm_token_usage")
public class LlmTokenUsage {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模型ID
     */
    private Long modelId;

    /**
     * 使用的token数量
     */
    private Integer tokensUsed;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
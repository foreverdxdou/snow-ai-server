package com.dxdou.snowai.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 问答请求模型
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "问答请求模型")
public class QaRequest {

    @Schema(description = "问题内容")
    private String question;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "请求ID")
    private String requestId;

    @Schema(description = "上下文消息列表")
    private String[] context;

    @Schema(description = "温度参数")
    private Double temperature;

    @Schema(description = "最大token数")
    private Integer maxTokens;

    @Schema(description = "llm配置主键")
    private Long llmId;
}
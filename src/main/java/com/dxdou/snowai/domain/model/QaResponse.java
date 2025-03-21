package com.dxdou.snowai.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 问答响应模型
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "问答响应模型")
public class QaResponse {

    @Schema(description = "回答内容")
    private String answer;

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "使用的token数")
    private Integer tokensUsed;

    @Schema(description = "处理时间(毫秒)")
    private Long processTime;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "错误信息")
    private String errorMessage;
}
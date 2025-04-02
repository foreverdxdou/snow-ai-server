package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库聊天历史VO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "知识库聊天历史VO类")
public class KbChatHistoryVO {

    /**
     * 聊天历史ID
     */
    @Schema(description = "聊天历史ID")
    private Long id;

    /**
     * 知识库ID
     */
    @Schema(description = "知识库ID")
    private String kbIds;

    /**
     * 会话ID
     */
    @Schema(description = "会话ID")
    private String sessionId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 问题
     */
    @Schema(description = "问题")
    private String question;

    /**
     * 答案
     */
    @Schema(description = "答案")
    private String answer;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
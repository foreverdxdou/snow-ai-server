package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库对话历史实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_chat_history")
public class KbChatHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sessionId;

    private String kbIds;

    private Long userId;

    private String question;

    private String answer;

    private Integer tokensUsed;

    private Long processTime;

    private String requestId;

    private Integer isStop;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否删除（0：未删除，1：已删除）
     */
    @TableLogic
    private Integer deleted;
}
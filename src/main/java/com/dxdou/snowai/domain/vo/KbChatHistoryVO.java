package com.dxdou.snowai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库聊天历史VO类
 *
 * @author foreverdxdou
 */
@Data
public class KbChatHistoryVO {

    /**
     * 聊天历史ID
     */
    private Long id;

    /**
     * 知识库ID
     */
    private Long knowledgeBaseId;

    /**
     * 知识库名称
     */
    private String knowledgeBaseName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbChatHistory;
import com.dxdou.snowai.domain.model.QaRequest;
import com.dxdou.snowai.domain.model.QaResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 知识库问答服务接口
 *
 * @author foreverdxdou
 */
public interface KbQaService {

    /**
     * 知识库问答
     *
     * @param kbIds    知识库ID列表
     * @param request 问答请求
     * @return 问答响应
     */
    QaResponse chat(Long[] kbIds, QaRequest request);

    /**
     * 知识库流式问答
     *
     * @param kbIds    知识库ID列表
     * @param request 问答请求
     * @param response 
     * @return SSE发射器
     */
    SseEmitter streamChat(Long[] kbIds, QaRequest request, HttpServletResponse response);

    /**
     * 通用问答
     *
     * @param request 问答请求
     * @return 问答响应
     */
    QaResponse generalChat(QaRequest request);

    /**
     * 通用流式问答
     *
     * @param request 问答请求
     * @param response 
     * @return SSE发射器
     */
    SseEmitter streamGeneralChat(QaRequest request);

    /**
     * 获取对话历史
     *
     * @param sessionId 会话ID
     * @return 对话历史
     */
    Page<KbChatHistory> getChatHistory(String sessionId);

    /**
     * 清除对话历史
     *
     * @param sessionId 会话ID
     */
    void clearChatHistory(String sessionId);

    /**
     * 删除对话历史
     *
     * @param historyId 历史记录ID
     */
    void deleteChatHistory(Long historyId);

    /**
     * 清除某个对话
     *
     * @param requestId 请求ID
     */
    void clearChatHistoryByRequestId(String requestId);

    /**
     * 查询用户的对话历史
     * @param userId
     * @return
     */
    List<KbChatHistory> getUserChatHistory(Long userId);

    /**
     * 清除用户对话历史
     * @param userId
     */
    void clearChatHistoryByUser(Long userId);

    SseEmitter sendMsg();
}
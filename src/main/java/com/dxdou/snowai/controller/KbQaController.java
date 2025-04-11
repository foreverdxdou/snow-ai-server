package com.dxdou.snowai.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.KbChatHistory;
import com.dxdou.snowai.domain.model.QaRequest;
import com.dxdou.snowai.domain.model.QaResponse;
import com.dxdou.snowai.domain.vo.KbChatHistoryVO;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbQaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 知识库问答系统控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库问答系统")
@RestController
@RequestMapping("/api/v1/kb/qa")
@RequiredArgsConstructor
public class KbQaController {

    private final KbQaService qaService;
    private final AuthService authService;

    @Operation(summary = "知识库问答")
    @PostMapping("/chat")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<QaResponse> chat(
            @Parameter(description = "知识库ID列表") @RequestParam(required = false) Long[] kbIds,
            @RequestBody QaRequest request) {
        return R.ok(qaService.chat(kbIds, request));
    }

    @Operation(summary = "知识库流式问答")
    @PostMapping("/chat/stream")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public SseEmitter streamChat(
            @Parameter(description = "知识库ID列表") @RequestParam(required = false) Long[] kbIds,
            @RequestBody QaRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-transform"); // 禁止代理修改
        return qaService.streamChat(kbIds, request,response);
    }

    @Operation(summary = "通用问答")
    @PostMapping("/general")
    @PreAuthorize("hasAuthority('kb:qa:general')")
    public R<QaResponse> generalChat(@RequestBody QaRequest request) {
        return R.ok(qaService.generalChat(request));
    }

    @Operation(summary = "通用流式问答")
    @PostMapping(value = "/general/stream")
    @PreAuthorize("hasAuthority('kb:qa:general')")
    public SseEmitter streamGeneralChat(@RequestBody QaRequest request, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-transform"); // 禁止代理修改
        return qaService.streamGeneralChat(request);
    }

    @Operation(summary = "获取用户对话历史列表")
    @GetMapping("/history/user")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<List<KbChatHistoryVO>> getUserChatHistory() {
        return R.ok(BeanUtil.copyToList(qaService.getUserChatHistory(authService.getCurrentUser().getId()), KbChatHistoryVO.class));
    }

    @Operation(summary = "获取对话历史")
    @GetMapping("/history")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<Page<KbChatHistoryVO>> getChatHistory(
            @Parameter(description = "会话ID") @RequestParam String sessionId) {
        Page<KbChatHistory> page = qaService.getChatHistory(sessionId);
        Page<KbChatHistoryVO> newPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        newPage.setRecords(BeanUtil.copyToList(page.getRecords(), KbChatHistoryVO.class));
        return R.ok(newPage);
    }

    @Operation(summary = "清除用户对话历史")
    @DeleteMapping("/clearChatHistoryByUser")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<Void> clearChatHistoryByUser() {
        qaService.clearChatHistoryByUser(authService.getCurrentUser().getId());
        return R.ok(null);
    }

    @Operation(summary = "清除对话历史")
    @DeleteMapping("/history/{sessionId}")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<Void> clearChatHistory(
            @Parameter(description = "会话ID") @PathVariable String sessionId) {
        qaService.clearChatHistory(sessionId);
        return R.ok(null);
    }

    @Operation(summary = "清除单个对话历史")
    @DeleteMapping("/clearChatHistoryByRequestId/{requestId}")
    @PreAuthorize("hasAuthority('kb:qa:chat')")
    public R<Void> clearChatHistoryByRequestId(
            @Parameter(description = "请求ID") @PathVariable String requestId) {
        qaService.clearChatHistoryByRequestId(requestId);
        return R.ok(null);
    }
}
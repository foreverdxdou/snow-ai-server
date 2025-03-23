package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.KbChatHistory;
import com.dxdou.snowai.domain.model.QaRequest;
import com.dxdou.snowai.domain.model.QaResponse;
import com.dxdou.snowai.service.KbQaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    @Operation(summary = "知识库问答")
    @PostMapping("/chat")
    public R<QaResponse> chat(
            @Parameter(description = "知识库ID") @RequestParam(required = false) Long kbId,
            @RequestBody QaRequest request) {
        return R.ok(qaService.chat(kbId, request));
    }

    @Operation(summary = "知识库流式问答")
    @PostMapping("/chat/stream")
    public R<SseEmitter> streamChat(
            @Parameter(description = "知识库ID") @RequestParam(required = false) Long kbId,
            @RequestBody QaRequest request) {
        return R.ok(qaService.streamChat(kbId, request));
    }

    @Operation(summary = "通用问答")
    @PostMapping("/general")
    public R<QaResponse> generalChat(@RequestBody QaRequest request) {
        return R.ok(qaService.generalChat(request));
    }

    @Operation(summary = "通用流式问答")
    @PostMapping("/general/stream")
    public R<SseEmitter> streamGeneralChat(@RequestBody QaRequest request) {
        return R.ok(qaService.streamGeneralChat(request));
    }

    @Operation(summary = "获取对话历史")
    @GetMapping("/history")
    public R<Page<KbChatHistory>> getChatHistory(
            @Parameter(description = "会话ID") @RequestParam String sessionId) {
        return R.ok(qaService.getChatHistory(sessionId));
    }

    @Operation(summary = "清除对话历史")
    @DeleteMapping("/history/{sessionId}")
    public R<Void> clearChatHistory(
            @Parameter(description = "会话ID") @PathVariable String sessionId) {
        qaService.clearChatHistory(sessionId);
        return R.ok(null);
    }
}
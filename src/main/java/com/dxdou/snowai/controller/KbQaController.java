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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.Duration;

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
    private final WebClient webClient;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChatGPTReply(@RequestBody QaRequest request) {
        String message= "";
        return qaService.streamChatGPTReply(message);

    }

    @GetMapping(value = "/stream1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChatGPTReply1(@RequestParam String message) {
        // 模拟调用 ChatGPT API 获取回复
        String reply = "收到消息: " + message + ". 正在思考...";
        return Flux.<String>create(sink -> {
                    sink.next(reply); // 先发送初始回复
                    // 模拟逐字生成回复
                    for (int i = 0; i < reply.length(); i++) {
                        try {
                            Thread.sleep(100); // 模拟延迟
                            sink.next(reply.substring(0, i + 1));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    sink.complete();
                })
                .map(data -> ServerSentEvent.<String>builder()
                        .data(data)
                        .build())
                .delayElements(Duration.ofMillis(100)); // 每隔一段时间发送一个字符
    }


    @Operation(summary = "知识库问答")
    @PostMapping("/chat")
    public R<QaResponse> chat(
            @Parameter(description = "知识库ID列表") @RequestParam(required = false) Long[] kbIds,
            @RequestBody QaRequest request) {
        return R.ok(qaService.chat(kbIds, request));
    }

    @Operation(summary = "知识库流式问答")
    @PostMapping("/chat/stream")
    public SseEmitter streamChat(
            @Parameter(description = "知识库ID列表") @RequestParam(required = false) Long[] kbIds,
            @RequestBody QaRequest request, HttpServletResponse response) {
        return qaService.streamChat(kbIds, request,response);
    }

    @Operation(summary = "通用问答")
    @PostMapping("/general")
    public R<QaResponse> generalChat(@RequestBody QaRequest request) {
        return R.ok(qaService.generalChat(request));
    }

    @Operation(summary = "通用流式问答")
    @PostMapping(value = "/general/stream", produces =  MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamGeneralChat(@RequestBody QaRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Accept-Encoding", "identity");
        response.setHeader("cache-control","no-cache");
        return qaService.streamGeneralChat(request);
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
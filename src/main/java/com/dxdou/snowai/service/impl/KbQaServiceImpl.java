package com.dxdou.snowai.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbChatHistory;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.LlmConfig;
import com.dxdou.snowai.domain.entity.LlmTokenUsage;
import com.dxdou.snowai.domain.model.QaRequest;
import com.dxdou.snowai.domain.model.QaResponse;
import com.dxdou.snowai.mapper.KbChatHistoryMapper;
import com.dxdou.snowai.mapper.LlmTokenUsageMapper;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.KbQaService;
import com.dxdou.snowai.service.KbSearchService;
import com.dxdou.snowai.service.LlmConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 知识库问答服务实现类
 *
 * @author foreverdxdou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KbQaServiceImpl extends ServiceImpl<KbChatHistoryMapper, KbChatHistory> implements KbQaService {

    private final KbChatHistoryMapper chatHistoryMapper;
    private final AuthService authService;
    private final KbSearchService searchService;
    private final LlmConfigService llmConfigService;
    private final LlmTokenUsageMapper tokenUsageMapper;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final Executor asyncExecutor;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QaResponse chat(Long[] kbIds, QaRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 获取默认的AI模型配置
            LlmConfig model = getDefaultModel();

            // 2. 检索相关文档
            List<KbDocument> relevantDocs = searchService
                    .semanticSearch(request.getQuestion(), kbIds, new Page<>(1, 5), null).getRecords().stream()
                    .map(vo -> {
                        KbDocument doc = new KbDocument();
                        doc.setId(vo.getId());
                        doc.setTitle(vo.getTitle());
                        doc.setContent(vo.getContent());
                        return doc;
                    }).collect(Collectors.toList());

            // 3. 构建提示词
            String prompt = buildPrompt(request.getQuestion(), relevantDocs);

            // 4. 调用AI模型
            String answer = callAiModel(model, prompt);

            // 5. 保存对话历史
            long processTime = System.currentTimeMillis() - startTime;
            saveChatHistory(request.getSessionId(), kbIds, request.getQuestion(), answer, model.getId(), processTime);

            // 6. 构建响应
            QaResponse response = new QaResponse();
            response.setSessionId(request.getSessionId());
            response.setSuccess(true);
            response.setAnswer(answer);
            response.setTokensUsed(100); // 从AI模型响应中获取实际使用的token数
            response.setProcessTime(processTime);

            return response;
        } catch (Exception e) {
            QaResponse response = new QaResponse();
            response.setSessionId(request.getSessionId());
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            response.setProcessTime(System.currentTimeMillis() - startTime);
            return response;
        }
    }

    @Override
    // @Transactional(rollbackFor = Exception.class)
    public SseEmitter streamChat(Long[] kbIds, QaRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();

        // 创建 SSE 发射器，设置超时时间为30分钟
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // 使用 AtomicBoolean 来控制流的状态
        AtomicBoolean isCompleted = new AtomicBoolean(false);

        // 保存当前的安全上下文
        SecurityContext context = SecurityContextHolder.getContext();
        StringBuilder answer = new StringBuilder();
        // 设置完成回调
        emitter.onCompletion(() -> {
            isCompleted.set(true);
            SecurityContextHolder.setContext(context);

            log.info("SSE connection completed");
            if (StringUtils.isNotBlank(answer.toString())) {
                // 5. 保存对话历史
                long processTime = System.currentTimeMillis() - startTime;
                saveChatHistory(request.getSessionId(), null, request.getQuestion(), answer.toString(), null,
                        processTime);
            }
        });

        // 设置超时回调
        emitter.onTimeout(() -> {
            isCompleted.set(true);
            emitter.complete();
        });

        // 设置错误回调
        emitter.onError(throwable -> {
            log.error("SSE error occurred", throwable);
            isCompleted.set(true);
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .name("error")
                        .data("Stream error occurred")
                        .build());
                emitter.complete();
            } catch (IOException e) {
                log.error("Error sending error event", e);
            }
        });

        // 异步处理流式响应
        CompletableFuture.runAsync(() -> {
            try {
                // 1. 获取默认的AI模型配置
                LlmConfig model = getDefaultModel();

                // 2. 检索相关文档
                List<KbDocument> relevantDocs = searchService
                        .semanticSearch(request.getQuestion(), kbIds, new Page<>(1, 5), null).getRecords().stream()
                        .map(vo -> {
                            KbDocument doc = new KbDocument();
                            doc.setId(vo.getId());
                            doc.setTitle(vo.getTitle());
                            doc.setContent(vo.getContent());
                            return doc;
                        }).collect(Collectors.toList());

                // 3. 构建提示词
                String prompt = buildPrompt(request.getQuestion(), relevantDocs);

                // 4. 构建请求体
                Map<String, Object> requestBody = buildRequestBody(model, prompt);

                // 5. 获取请求头
                Map<String, String> headers = getHeaders(model);

                // 6. 发送请求并处理流式响应
                webClient.post()
                        .uri(model.getApiUrl())
                        .headers(h -> {
                            headers.forEach(h::add);
                            h.setCacheControl("no-cache");
                            h.setPragma("no-cache");
                            h.set("Accept-Encoding", "identity"); // 禁止 WebClient 请求压缩
                        })
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToFlux(String.class)
                        .doOnNext(chunk -> {
                            if (isCompleted.get()) {
                                return;
                            }

                            try {
                                if ("[DONE]".equals(chunk)) {
                                    if (!isCompleted.get()) {
                                        emitter.send(SseEmitter.event()
                                                .id(String.valueOf(System.currentTimeMillis()))
                                                .name("done")
                                                .data("[DONE]")
                                                .build());
                                    }
                                    return;
                                }

                                Map<String, Object> chunkResponse = parseJson(chunk);
                                if (chunkResponse != null && chunkResponse.containsKey("choices")) {
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> choices = (List<Map<String, Object>>) chunkResponse.get("choices");
                                    if (!choices.isEmpty()) {
                                        Map<String, Object> choice = choices.get(0);
                                        if (choice.containsKey("delta") && choice.get("delta") instanceof Map) {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                                            if (delta.containsKey("content")) {
                                                String content = (String) delta.get("content");
                                                if (StringUtils.isNotBlank(content)) {
                                                }
                                                answer.append(content);
                                                if (!isCompleted.get()) {
                                                    emitter.send(SseEmitter.event()
                                                            .id(String.valueOf(System.currentTimeMillis()))
                                                            .name("message")
                                                            .data(content)
                                                            .build());
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Error processing chunk", e);
                                if (!isCompleted.get()) {
                                    try {
                                        emitter.send(SseEmitter.event()
                                                .id(String.valueOf(System.currentTimeMillis()))
                                                .name("error")
                                                .data("Error processing chunk: " + e.getMessage())
                                                .build());
                                    } catch (IOException ex) {
                                        log.error("Error sending error event", ex);
                                    }
                                }
                            }
                        })
                        .doOnComplete(() -> {
                            if (!isCompleted.get()) {
                                try {
                                    emitter.send(SseEmitter.event()
                                            .id(String.valueOf(System.currentTimeMillis()))
                                            .name("complete")
                                            .data("Stream completed")
                                            .build());
                                    emitter.complete();
                                } catch (IOException e) {
                                    log.error("Error completing stream", e);
                                }
                            }
                        })
                        .doOnError(error -> {
                            log.error("Stream error", error);
                            if (!isCompleted.get()) {
                                try {
                                    emitter.send(SseEmitter.event()
                                            .id(String.valueOf(System.currentTimeMillis()))
                                            .name("error")
                                            .data("Stream error: " + error.getMessage())
                                            .build());
                                    emitter.complete();
                                } catch (IOException e) {
                                    log.error("Error sending error event", e);
                                }
                            }
                        })
                        .subscribe();
            } catch (Exception e) {
                log.error("Error in stream processing", e);
                if (!isCompleted.get()) {
                    try {
                        emitter.send(SseEmitter.event()
                                .id(String.valueOf(System.currentTimeMillis()))
                                .name("error")
                                .data("Stream processing error: " + e.getMessage())
                                .build());
                        emitter.complete();
                    } catch (IOException ex) {
                        log.error("Error sending error event", ex);
                    }
                }
            }
        }, asyncExecutor);
        return emitter;
    }

    @Override
    public QaResponse generalChat(QaRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 获取默认的AI模型配置
            LlmConfig model = getDefaultModel();

            // 2. 构建提示词
            String prompt = buildGeneralPrompt(request.getQuestion());

            // 3. 调用AI模型
            String answer = callAiModel(model, prompt);

            // 4. 构建响应
            long processTime = System.currentTimeMillis() - startTime;
            QaResponse response = new QaResponse();
            response.setSessionId(request.getSessionId());
            response.setSuccess(true);
            response.setAnswer(answer);
            response.setTokensUsed(100); // 从AI模型响应中获取实际使用的token数
            response.setProcessTime(processTime);

            return response;
        } catch (Exception e) {
            QaResponse response = new QaResponse();
            response.setSessionId(request.getSessionId());
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            response.setProcessTime(System.currentTimeMillis() - startTime);
            return response;
        }
    }

    @Override
    public SseEmitter streamGeneralChat(QaRequest request) {
        long startTime = System.currentTimeMillis();
        // 创建 SSE 发射器，设置超时时间为30分钟
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // 使用 AtomicBoolean 来控制流的状态
        AtomicBoolean isCompleted = new AtomicBoolean(false);

        // 保存当前的安全上下文
        SecurityContext context = SecurityContextHolder.getContext();
        StringBuilder answer = new StringBuilder();
        // 设置完成回调
        emitter.onCompletion(() -> {
            isCompleted.set(true);
            SecurityContextHolder.setContext(context);

            log.info("SSE connection completed");
            if (StringUtils.isNotBlank(answer.toString())) {
                // 5. 保存对话历史
                long processTime = System.currentTimeMillis() - startTime;
                saveChatHistory(request.getSessionId(), null, request.getQuestion(), answer.toString(), null,
                        processTime);
            }
        });

        // 设置超时回调
        emitter.onTimeout(() -> {
            isCompleted.set(true);
            emitter.complete();
        });

        // 设置错误回调
        emitter.onError(throwable -> {
            log.error("SSE error occurred", throwable);
            isCompleted.set(true);
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(System.currentTimeMillis()))
                        .name("error")
                        .data("Stream error occurred")
                        .build());
                emitter.complete();
            } catch (IOException e) {
                log.error("Error sending error event", e);
            }
        });

        // 异步处理流式响应
        CompletableFuture.runAsync(() -> {
            try {
                // 1. 获取默认的AI模型配置
                LlmConfig model = getDefaultModel();

                // 2. 构建提示词
                String prompt = buildGeneralPrompt(request.getQuestion());

                // 3. 构建请求体
                Map<String, Object> requestBody = buildRequestBody(model, prompt);

                // 4. 获取请求头
                Map<String, String> headers = getHeaders(model);

                // 5. 发送请求并处理流式响应
                webClient.post()
                        .uri(model.getApiUrl())
                        .headers(h -> {
                            headers.forEach(h::add);
                            h.setCacheControl("no-cache");
                            h.setPragma("no-cache");
                            h.set("Accept-Encoding", "identity"); // 禁止 WebClient 请求压缩
                        })
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToFlux(String.class)
                        .doOnNext(chunk -> {
                            if (isCompleted.get()) {
                                return;
                            }

                            try {
                                if ("[DONE]".equals(chunk)) {
                                    if (!isCompleted.get()) {
                                        emitter.send(SseEmitter.event()
                                                .id(String.valueOf(System.currentTimeMillis()))
                                                .name("done")
                                                .data("[DONE]")
                                                .build());
                                    }
                                    return;
                                }

                                Map<String, Object> chunkResponse = parseJson(chunk);
                                if (chunkResponse != null && chunkResponse.containsKey("choices")) {
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> choices = (List<Map<String, Object>>) chunkResponse.get("choices");
                                    if (!choices.isEmpty()) {
                                        Map<String, Object> choice = choices.get(0);
                                        if (choice.containsKey("delta") && choice.get("delta") instanceof Map) {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                                            if (delta.containsKey("content")) {
                                                String content = (String) delta.get("content");
                                                if (StringUtils.isNotBlank(content)) {
                                                }
                                                answer.append(content);
                                                if (!isCompleted.get()) {
                                                    emitter.send(SseEmitter.event()
                                                            .id(String.valueOf(System.currentTimeMillis()))
                                                            .name("message")
                                                            .data(content)
                                                            .build());
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Error processing chunk", e);
                                if (!isCompleted.get()) {
                                    try {
                                        emitter.send(SseEmitter.event()
                                                .id(String.valueOf(System.currentTimeMillis()))
                                                .name("error")
                                                .data("Error processing chunk: " + e.getMessage())
                                                .build());
                                    } catch (IOException ex) {
                                        log.error("Error sending error event", ex);
                                    }
                                }
                            }
                        })
                        .doOnComplete(() -> {
                            if (!isCompleted.get()) {
                                try {
                                    emitter.send(SseEmitter.event()
                                            .id(String.valueOf(System.currentTimeMillis()))
                                            .name("complete")
                                            .data("Stream completed")
                                            .build());
                                    emitter.complete();
                                } catch (IOException e) {
                                    log.error("Error completing stream", e);
                                }
                            }
                        })
                        .doOnError(error -> {
                            log.error("Stream error", error);
                            if (!isCompleted.get()) {
                                try {
                                    emitter.send(SseEmitter.event()
                                            .id(String.valueOf(System.currentTimeMillis()))
                                            .name("error")
                                            .data("Stream error: " + error.getMessage())
                                            .build());
                                    emitter.complete();
                                } catch (IOException e) {
                                    log.error("Error sending error event", e);
                                }
                            }
                        })
                        .subscribe();
            } catch (Exception e) {
                log.error("Error in stream processing", e);
                if (!isCompleted.get()) {
                    try {
                        emitter.send(SseEmitter.event()
                                .id(String.valueOf(System.currentTimeMillis()))
                                .name("error")
                                .data("Stream processing error: " + e.getMessage())
                                .build());
                        emitter.complete();
                    } catch (IOException ex) {
                        log.error("Error sending error event", ex);
                    }
                }
            }
        }, asyncExecutor);

        return emitter;
    }

    @Override
    public Page<KbChatHistory> getChatHistory(String sessionId) {
        Page<KbChatHistory> page = new Page<>(1, 10);
        LambdaQueryWrapper<KbChatHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbChatHistory::getSessionId, sessionId).orderByDesc(KbChatHistory::getCreateTime);
        return chatHistoryMapper.selectPage(page, wrapper);
    }

    @Override
    public void clearChatHistory(String sessionId) {
        LambdaQueryWrapper<KbChatHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbChatHistory::getSessionId, sessionId);
        chatHistoryMapper.delete(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteChatHistory(Long historyId) {
        chatHistoryMapper.deleteById(historyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearChatHistory(Long kbId) {
        chatHistoryMapper.deleteByKnowledgeBaseId(kbId);
    }

    @Override
    public Flux<ServerSentEvent<String>> streamChatGPTReply(String message) {
        // 1. 获取默认的AI模型配置
        LlmConfig model = getDefaultModel();

        // 2. 构建提示词
        String prompt = buildGeneralPrompt("人生的意义");

        // 3. 构建请求体
        Map<String, Object> requestBody = buildRequestBody(model, prompt);

        // 4. 获取请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + model.getApiKey());
        // 使用 WebClient 异步调用外部 API
        return webClient.post()
                .uri(model.getApiUrl())
                .headers(h -> {
                    h.addAll(headers);
                    // 添加必要的响应头
                    h.setCacheControl("no-cache");
                    h.setPragma("no-cache");
                })
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class) // 假设 API 返回 String 类型数据
                .map(data -> ServerSentEvent.<String>builder()
                        .data(data) // 将 API 响应数据包装到 SSE 事件中
                        .build())
                .delayElements(Duration.ofMillis(100));
    }

    /**
     * 获取默认的AI模型配置
     *
     * @return AI模型配置
     */
    private LlmConfig getDefaultModel() {
        // 1. 查询启用的模型配置
        LambdaQueryWrapper<LlmConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LlmConfig::getEnabled, true);
        List<LlmConfig> models = llmConfigService.list(wrapper);

        if (models.isEmpty()) {
            throw new BusinessException("未找到可用的AI模型配置");
        }

        // 2. 返回第一个启用的模型配置
        return models.get(0);
    }

    /**
     * 构建提示词
     *
     * @param question     问题
     * @param relevantDocs 相关文档
     * @return 提示词
     */
    private String buildPrompt(String question, List<KbDocument> relevantDocs) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("基于以下参考文档回答问题：\n\n");

        // 添加参考文档
        for (KbDocument doc : relevantDocs) {
            prompt.append("文档标题：").append(doc.getTitle()).append("\n");
            prompt.append("文档内容：").append(doc.getContent()).append("\n\n");
        }

        prompt.append("问题：").append(question).append("\n");
        prompt.append("请基于上述参考文档，给出准确、简洁的回答。");

        return prompt.toString();
    }

    /**
     * 构建通用提示词
     *
     * @param question 问题
     * @return 提示词
     */
    private String buildGeneralPrompt(String question) {
        return "请回答以下问题：\n\n" + question;
    }

    /**
     * 调用AI模型
     *
     * @param model  AI模型
     * @param prompt 提示词
     * @return 回答
     */
    private String callAiModel(LlmConfig model, String prompt) {
        // 1. 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + model.getApiKey());

        // 2. 构建请求体
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model.getModelName());
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        requestBody.put("stream", false);

        // 3. 发送请求
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        Map<String, Object> response = restTemplate.postForObject(model.getApiUrl(), request, Map.class);

        // 4. 解析响应
        if (response == null) {
            throw new BusinessException("AI模型调用失败");
        }

        // 5. 获取回答和token使用情况
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choicesList = (List<Map<String, Object>>) response.get("choices");
        if (choicesList == null || choicesList.isEmpty()) {
            throw new BusinessException("AI模型响应格式错误");
        }
        Map<String, Object> choice = choicesList.get(0);
        String answer = (String) choice.get("text");

        @SuppressWarnings("unchecked")
        Map<String, Object> usage = (Map<String, Object>) response.get("usage");
        if (usage == null) {
            throw new BusinessException("AI模型响应中缺少token使用信息");
        }
        int tokensUsed = (int) usage.get("total_tokens");

        // 6. 保存token使用情况
        saveTokenUsage(model.getId(), tokensUsed);

        return answer;
    }

    /**
     * 流式调用AI模型
     *
     * @param model    AI模型
     * @param prompt   提示词
     * @param callback 回调函数
     */
    private void streamCallAiModel(LlmConfig model, String prompt, HttpServletResponse servletResponse, SseEmitter emitter,
                                   java.util.function.Consumer<String> callback) {
        servletResponse.setContentType("text/event-stream");
        servletResponse.setHeader("Cache-Control", "no-cache");
        servletResponse.setHeader("content-encoding", "br");
        servletResponse.setCharacterEncoding("UTF-8");
        // 1. 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + model.getApiKey());

        // 2. 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", model.getModelName());
        requestBody.put("messages", new JSONObject[]{new JSONObject().put("role", "system").put("content", ""),
                new JSONObject().put("role", "user").put("content", prompt)});
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        requestBody.put("stream", true);

        log.debug("streamCallAiModel requestBody: " + requestBody.toJSONString(0));

        // 保存当前SecurityContext
        SecurityContext context = SecurityContextHolder.getContext();

        emitter.onCompletion(() -> {
            // 恢复上下文（例如在发送事件时）
            SecurityContextHolder.setContext(context);
        });

        // 3. 发送请求并处理流式响应
        webClient.post().uri(model.getApiUrl())
                .headers(h -> {
                    h.addAll(headers);
                    // 添加必要的响应头
                    h.setCacheControl("no-cache");
                    h.setPragma("no-cache");
                })
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(chunk -> {
                    try {
                        if ("[DONE]".equals(chunk)) {
                            emitter.send(SseEmitter.event()
                                    .id(String.valueOf(System.currentTimeMillis()))
                                    .name("done")
                                    .data("[DONE]")
                                    .build());
                            return;
                        }

                        Map<String, Object> response = parseJson(chunk);
                        if (response != null && response.containsKey("choices")) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                            if (!choices.isEmpty()) {
                                Map<String, Object> choice = choices.get(0);
                                if (choice.containsKey("delta") && choice.get("delta") instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> delta = (Map<String, Object>) choice.get("delta");
                                    if (delta.containsKey("content")) {
                                        String content = (String) delta.get("content");
                                        log.info("streamCallAiModel content: " + content);
                                        callback.accept(content);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("处理流式响应失败", e);
                        emitter.completeWithError(e);
                    }
                })
                .doOnComplete(() -> {
                    try {
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("完成流式响应失败", e);
                    }
                })
                .doOnError(error -> {
                    log.error("流式响应发生错误", error);
                    emitter.completeWithError(error);
                })
                .subscribe();
    }

    /**
     * 保存token使用情况
     *
     * @param modelId    模型ID
     * @param tokensUsed token使用数量
     */
    private void saveTokenUsage(Long modelId, int tokensUsed) {
        LlmTokenUsage usage = new LlmTokenUsage();
        usage.setModelId(modelId);
        usage.setTokensUsed(tokensUsed);
        usage.setCreateTime(LocalDateTime.now());
        usage.setUpdateTime(LocalDateTime.now());
        tokenUsageMapper.insert(usage);
    }

    /**
     * 解析JSON字符串
     *
     * @param json JSON字符串
     * @return Map对象
     */
    private Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new BusinessException("解析JSON失败: " + e.getMessage());
        }
    }

    /**
     * 保存对话历史
     *
     * @param sessionId   会话ID
     * @param kbIds       知识库ID列表
     * @param question    问题
     * @param answer      回答
     * @param modelId     模型ID
     * @param processTime 处理时间
     */
    private void saveChatHistory(String sessionId, Long[] kbIds, String question, String answer, Long modelId,
                                 long processTime) {
        KbChatHistory history = new KbChatHistory();
        history.setSessionId(sessionId);
        history.setKbIds(kbIds != null && kbIds.length > 0 ? CollUtil.join(Arrays.asList(kbIds), ",") : null);
        history.setQuestion(question);
        history.setAnswer(answer);
        history.setTokensUsed(100); // 从AI模型响应中获取实际使用的token数
        history.setProcessTime(processTime);
        history.setCreateTime(LocalDateTime.now());
        history.setUpdateTime(LocalDateTime.now());
        Long userId = authService.getCurrentUser().getId();
        history.setUserId(userId);
        try {
            chatHistoryMapper.insert(history);
        } catch (Exception e) {
            log.error("保存对话历史失败", e);
        }
    }

    private Map<String, String> getHeaders(LlmConfig model) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + model.getApiKey());
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private Map<String, Object> buildRequestBody(LlmConfig model, String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model.getModelName());
        requestBody.put("messages", new JSONObject[]{new JSONObject().put("role", "system").put("content", ""),
                new JSONObject().put("role", "user").put("content", prompt)});
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);
        requestBody.put("stream", true);
        return requestBody;
    }
}
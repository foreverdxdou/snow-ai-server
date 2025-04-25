package com.dxdou.snowai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.dxdou.snowai.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.SignalType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    private final SystemConfigService systemConfigService;
    private final EmbeddingConfigService embeddingConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QaResponse chat(Long[] kbIds, QaRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 获取默认的AI模型配置
            LlmConfig model = getDefaultModel(request.getLlmId());

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
            saveChatHistory(request.getSessionId(), kbIds, request.getQuestion(), answer, model.getId(), processTime, request.getRequestId(), false);

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
    public SseEmitter streamChat(Long[] kbIds, QaRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();

        // 创建 SSE 发射器，设置超时时间为30分钟
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // 使用 AtomicBoolean 来控制流的状态
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        AtomicBoolean isStop = new AtomicBoolean(false);

        // 保存当前的安全上下文
        SecurityContext context = SecurityContextHolder.getContext();
        StringBuilder answer = new StringBuilder();
        List<String> answerList = new ArrayList<>();
        // 设置完成回调
        emitter.onCompletion(() -> {
            isCompleted.set(true);
            SecurityContextHolder.setContext(context);

            log.info("SSE connection completed");
            if (StringUtils.isNotBlank(answer.toString())) {
                // 5. 保存对话历史
                long processTime = System.currentTimeMillis() - startTime;
                saveChatHistory(request.getSessionId(), null, request.getQuestion(), answer.toString(), null,
                        processTime, request.getRequestId(), isStop.get());
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
            if (StringUtils.contains(throwable.getMessage(), "你的主机中的软件中止了一个已建立的连接")) {
                isStop.set(true);
            }
            isCompleted.set(true);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "Stream error occurred");
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data(jsonObject)
                        .build());
                emitter.completeWithError(throwable);
            } catch (IOException e) {
                log.error("Error sending error event", e);
            }
        });

        // 异步处理流式响应
        CompletableFuture.runAsync(() -> {
            // 2. 获取系统配置，判断使用NLP还是Embedding
            String searchType = systemConfigService.getConfigValue("kb.search.type", "NLP");
            List<KbDocument> relevantDocs;
            if ("EMBEDDING".equals(searchType)) {
                // 使用Embedding进行检索
                relevantDocs = searchService
                        .embeddingSearch(request.getQuestion(), kbIds, new Page<>(1, 5), null).getRecords().stream()
                        .map(vo -> {
                            KbDocument doc = new KbDocument();
                            doc.setId(vo.getId());
                            doc.setTitle(vo.getTitle());
                            doc.setContent(vo.getContent());
                            return doc;
                        }).collect(Collectors.toList());
            } else {
                // 使用NLP进行检索
                relevantDocs = searchService
                        .semanticSearch(request.getQuestion(), kbIds, new Page<>(1, 5), null).getRecords().stream()
                        .map(vo -> {
                            KbDocument doc = new KbDocument();
                            doc.setId(vo.getId());
                            doc.setTitle(vo.getTitle());
                            doc.setContent(vo.getContent());
                            return doc;
                        }).collect(Collectors.toList());
            }
            sendRequestForStream(request, isCompleted, emitter, answer, relevantDocs, isStop, answerList);
        }, asyncExecutor);

        return emitter;
    }

    @Override
    public QaResponse generalChat(QaRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 1. 获取默认的AI模型配置
            LlmConfig model = getDefaultModel(request.getLlmId());

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
        AtomicBoolean isStop = new AtomicBoolean(false);

        // 保存当前的安全上下文
        SecurityContext context = SecurityContextHolder.getContext();
        StringBuilder answer = new StringBuilder();
        List<String> answerList = new ArrayList<>();
        // 设置完成回调
        emitter.onCompletion(() -> {
            isCompleted.set(true);
            SecurityContextHolder.setContext(context);

            log.info("answerList---{}", JSON.toJSONString(answerList));

            log.info("SSE connection completed");
            if (StringUtils.isNotBlank(answer.toString())) {
                // 5. 保存对话历史
                long processTime = System.currentTimeMillis() - startTime;
                saveChatHistory(request.getSessionId(), null, request.getQuestion(), answer.toString(), null,
                        processTime, request.getRequestId(), isStop.get());
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
            if (StringUtils.contains(throwable.getMessage(), "你的主机中的软件中止了一个已建立的连接")) {
                isStop.set(true);
            }
            isCompleted.set(true);
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "Stream error occurred");
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data(jsonObject)
                        .build());
                emitter.completeWithError(throwable);
            } catch (IOException e) {
                log.error("Error sending error event", e);
            }
        });

        // 异步处理流式响应
        CompletableFuture.runAsync(() -> {
            sendRequestForStream(request, isCompleted, emitter, answer, null, isStop, answerList);
        }, asyncExecutor);

        return emitter;
    }

    private void sendRequestForStream(QaRequest request, AtomicBoolean isCompleted, SseEmitter emitter,
                                      StringBuilder answer, List<KbDocument> relevantDocs, AtomicBoolean isStop, List<String> answerList) {
        try {
            // 1. 获取默认的AI模型配置
            LlmConfig model = getDefaultModel(request.getLlmId());

            // 2. 构建提示词
            String prompt;
            if (CollUtil.isNotEmpty(relevantDocs)) {
                prompt = buildPrompt(request.getQuestion(), relevantDocs);
            } else {
                prompt = buildGeneralPrompt(request.getQuestion());
            }

            // 3. 构建请求体
            Map<String, Object> requestBody = buildRequestBody(model, prompt);

            log.info("对话 Request body: {}", JSON.toJSONString(requestBody));
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
                    .bodyValue(JSON.toJSONString(requestBody))
                    .retrieve()
                    .bodyToFlux(String.class)
                    .takeWhile(chunk -> !isCompleted.get())
                    .doOnNext(chunk -> {
                        log.info("对话 Response chunk: {}", chunk);
                        if (isCompleted.get()) {
                            return;
                        }
                        processStreamResponse(chunk, isCompleted, emitter, answer, model, answerList);
                    })
                    .doOnCancel(() -> {
                        log.info("对话流被取消--doOnCancel");
                    })
                    .doFinally(signalType -> {
                        if (signalType == SignalType.CANCEL) {
                            log.info("对话流被取消--doFinally");
                        } else {
                            log.info("对话流正常结束--doFinally---" + signalType.name());
                        }
                    })
                    .doOnComplete(() -> {
                        if (!isCompleted.get()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("content", "Stream completed");
                                emitter.send(SseEmitter.event()
                                        .name("complete")
                                        .data(jsonObject)
                                        .build());
                                emitter.complete();
                            } catch (IOException e) {
                                log.error("Error completing stream", e);
                            }
                        }
                    })
                    .doOnCancel(() -> {
                        log.info("对话流被取消--doOnCancel");
                    })
                    .doOnError(error -> {
                        log.error("Stream error", error);
                        if (!isCompleted.get()) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("content", "Stream error: " + error.getMessage());
                                emitter.send(SseEmitter.event()
                                        .name("error")
                                        .data(jsonObject)
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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", "Stream processing error: " + e.getMessage());
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(jsonObject)
                            .build());
                    emitter.complete();
                } catch (IOException ex) {
                    log.error("Error sending error event", ex);
                }
            }
        }
    }

    @SneakyThrows
    private void processStreamResponse(String chunk, AtomicBoolean isCompleted, SseEmitter emitter,
                                       StringBuilder answer, LlmConfig model, List<String> answerList) {
        try {
            if ("[DONE]".equals(chunk)) {
                if (!isCompleted.get()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", "[DONE]");
                    emitter.send(SseEmitter.event()
                            .name("done")
                            .data(jsonObject)
                            .build());
                }
                return;
            }

            Map<String, Object> chunkResponse = parseJson(chunk);
            if (chunkResponse == null) {
                return;
            }

            // switch (model.getModelProvider().toLowerCase()) {
            //     case "openai":
            //     case "anthropic":
            //     case "google":
            //     case "meta":
            //     case "microsoft":
            //     case "amazon":
            //     case "baidu":
            //     case "alibaba":
            //     case "tencent":
            //     case "zhipu":
            //     case "minimax":
            //     case "moonshot":
            //     case "deepseek":
            //     case "other":
            //     default:
            //         processDefaultResponse(chunkResponse, isCompleted, emitter, answer);
            //         break;
            // }

            // 根据模型类型处理响应
            if ("GENERAL".equals(model.getModelType())) {
                processDefaultResponse(chunkResponse, isCompleted, emitter, answer, answerList);
            } else if ("REASONING".equals(model.getModelType())) {
                processReasoningModelResponse(chunkResponse, isCompleted, emitter, answer, answerList);
            } else {
                // 默认处理方式
                processDefaultResponse(chunkResponse, isCompleted, emitter, answer, answerList);
            }
        } catch (ClientAbortException e) {
            throw e;
        } catch (Exception e) {
            log.error("处理流式响应失败", e);
            if (!isCompleted.get()) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("content", "处理响应失败: " + e.getMessage());
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data(jsonObject)
                            .build());
                } catch (IOException ex) {
                    log.error("发送错误事件失败", ex);
                }
            }
        }
    }


    public static final String[] choiceMsgs = {"message", "delta"};
    public static final String reasoningCol = "reasoning_content";
    public static final String reasoningEvent = "reasoning";
    public static final String contentCol = "content";
    public static final String choicesCol = "choices";

    /**
     * 处理推理模型的流式响应
     * 推理模型通常返回结构化的推理过程和结论
     */
    private void processReasoningModelResponse(Map<String, Object> chunkResponse, AtomicBoolean isCompleted,
                                               SseEmitter emitter, StringBuilder answer, List<String> answerList) throws IOException {
        // 处理标准OpenAI格式的响应
        if (chunkResponse.containsKey(choicesCol)) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) chunkResponse.get(choicesCol);
            if (!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                for (String choiceMsg : choiceMsgs) {
                    if (choice.containsKey(choiceMsg) && choice.get(choiceMsg) instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> delta = (Map<String, Object>) choice.get(choiceMsg);
                        if (delta.containsKey(reasoningCol) && delta.get(reasoningCol) != null && StringUtils.isNotBlank(delta.get(reasoningCol).toString())) {
                            String content = (String) delta.get(reasoningCol);
                            if (StringUtils.isNotBlank(content)) {
                                if (answer.isEmpty()) {
                                    content = "<think>" + content;
                                }
                                answerList.add(content);
                                answer.append(content);
                                if (!isCompleted.get()) {
                                    log.info("推理过程: " + content);
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("content", content);
                                    emitter.send(SseEmitter.event()
                                            .name(choiceMsgs[0])
                                            .data(jsonObject)
                                            .build());
                                }
                            }
                        }
                        if (delta.containsKey(contentCol) && (!delta.containsKey(reasoningCol) || delta.get(reasoningCol) == null)) {
                            String content = (String) delta.get(contentCol);
                            if (!answer.isEmpty() && answer.toString().contains("<think>") && !answer.toString().contains("</think>")) {
                                content = "</think>" + content;
                            }
                            answerList.add(content);
                            answer.append(content);
                            log.info("结论: " + content);
                            if (!isCompleted.get()) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("content", content);
                                emitter.send(SseEmitter.event()
                                        .name(choiceMsgs[0])
                                        .data(jsonObject)
                                        .build());
                            }
                        }
                    }
                }
            }
        } else {
            // 如果响应中没有reasoning字段，使用默认处理方式
            processDefaultResponse(chunkResponse, isCompleted, emitter, answer, answerList);
        }
    }

    /**
     * 默认的响应处理方式
     */
    private void processDefaultResponse(Map<String, Object> chunkResponse, AtomicBoolean isCompleted,
                                        SseEmitter emitter, StringBuilder answer, List<String> answerList) throws IOException {
        // 处理标准OpenAI格式的响应
        if (chunkResponse.containsKey(choicesCol)) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> choices = (List<Map<String, Object>>) chunkResponse.get(choicesCol);
            if (!choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);
                if (choice.containsKey(choiceMsgs[1]) && choice.get(choiceMsgs[1]) instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> delta = (Map<String, Object>) choice.get(choiceMsgs[1]);
                    if (delta.containsKey(contentCol)) {
                        String content = (String) delta.get(contentCol);
                        answer.append(content);
                        answerList.add(content);
                        if (!isCompleted.get()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("content", content);
                            emitter.send(SseEmitter.event()
                                    .name(choiceMsgs[0])
                                    .data(jsonObject)
                                    .build());
                        }
                    }
                }
            }
        }

        // 处理其他可能的响应格式
        if (chunkResponse.containsKey("response")) {
            if (chunkResponse.containsKey("done")) {
                if ((Boolean) chunkResponse.get("done")) {
                    if (!isCompleted.get()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("content", "[DONE]");
                        emitter.send(SseEmitter.event()
                                .name("done")
                                .data(jsonObject)
                                .build());
                    }
                }
            }
            String response = (String) chunkResponse.get("response");
            answer.append(response);
            answerList.add(response);
            if (!isCompleted.get()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", response);
                emitter.send(SseEmitter.event()
                        .name(choiceMsgs[0])
                        .data(jsonObject)
                        .build());
            }
        }
    }

    @Override
    public Page<KbChatHistory> getChatHistory(String sessionId) {
        Page<KbChatHistory> page = new Page<>(1, 100);
        LambdaQueryWrapper<KbChatHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KbChatHistory::getSessionId, sessionId).orderByAsc(KbChatHistory::getCreateTime);
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
    public void clearChatHistoryByRequestId(String requestId) {
        chatHistoryMapper.deleteByRequestId(requestId);
    }

    @Override
    public List<KbChatHistory> getUserChatHistory(Long userId) {
        return chatHistoryMapper.selectUserChatHistory(userId);
    }

    @Override
    public void clearChatHistoryByUser(Long userId) {
        chatHistoryMapper.clearChatHistoryByUser(userId);
    }

    @Override
    public SseEmitter sendMsg() {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        // 设置完成回调
        emitter.onCompletion(() -> {

        });

        // 设置超时回调
        emitter.onTimeout(() -> {
            emitter.complete();
        });


        // 设置错误回调
        emitter.onError(throwable -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "Stream error occurred");
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data(jsonObject)
                        .build());
                emitter.completeWithError(throwable);
            } catch (IOException e) {

            }
        });
        CompletableFuture.runAsync(() -> {
            String textArray = "[\"<think>好的\",\"结合\",\"代码\",\"示例\",\"，\",\"说明\",\"如何\",\"实现\",\"流\",\"式\",\"输出的\",\"滚动\",\"动画\",\"效果\",\"。\",\"可能需要\",\"分\",\"后端\",\"和\",\"前端\",\"部分\",\"，\",\"但\",\"用户\",\"可能\",\"更\",\"关注\",\"前端\",\"实现\",\"，\",\"假设\",\"后端\",\"已经\",\"处理\",\"了\",\"流\",\"式\",\"数据的\",\"推送\",\"。\",\"</think>以下是\",\"结合\",\"代码\",\"实现\",\" Gro\",\"k\",\" \",\"流\",\"式\",\"输出\",\"滚动\",\"推理\",\"动画\",\"的技术\",\"方案\",\"，\",\"我将\",\"通过\",\"分\",\"步\",\"说明\",\"和\",\"代码\",\"注释\",\"为您\",\"讲解\",\"关键\",\"实现\",\"点\",\"：\\n\\n\",\"---\\n\\n\",\"###\",\" \",\"一\",\"、\",\"技术\",\"实现\",\"原理\",\"\\n\\n\",\"![\",\"流\",\"式\",\"输出\",\"示意图\",\"](\",\"https\",\"://\",\"example\",\".com\",\"/\",\"stream\",\"ing\",\"-an\",\"imation\",\".png\",\")\\n\",\"1\",\".\",\" **\",\"数据\",\"流\",\"传输\",\"**\",\"：\",\"使用\",\" Server\",\"-S\",\"ent\",\" Events\",\" (\",\"SS\",\"E\",\")\",\" \",\"实现\",\"服务器\",\"到\",\"客户\",\"端的\",\"持续\",\"数据\",\"推送\",\"\\n\",\"2\",\".\",\" **\",\"动态\",\"渲染\",\"**\",\"：\",\"通过\",\" JavaScript\",\" \",\"动态\",\"追加\",\"内容\",\"元素\",\"\\n\",\"3\",\".\",\" **\",\"滚动\",\"控制\",\"**\",\"：\",\"使用\",\" CSS\",\" \",\"动画\",\" +\",\" JavaScript\",\" \",\"滚动\",\" API\",\" \",\"实现\",\"平滑\",\"滚动\",\"效果\",\"\\n\\n\",\"---\\n\\n\",\"###\",\" \",\"二\",\"、\",\"完整\",\"代码\",\"示例\",\"\\n\\n\",\"```\",\"html\",\"\\n\",\"<!--\",\" \",\"前端\",\"部分\",\" -->\\n\",\"<div\",\" id\",\"=\\\"\",\"g\",\"rok\",\"-output\",\"\\\"\",\" class\",\"=\\\"\",\"stream\",\"-container\",\"\\\"></\",\"div\",\">\\n\\n\",\"<style\",\">\\n\",\"/*\",\" \",\"容器\",\"样式\",\"与\",\"滚动\",\"动画\",\" */\\n\",\".stream\",\"-container\",\" {\\n\",\" \",\" height\",\":\",\" \",\"60\",\"vh\",\";\\n\",\" \",\" overflow\",\"-y\",\":\",\" auto\",\";\\n\",\" \",\" scroll\",\"-be\",\"havior\",\":\",\" smooth\",\";\",\" /*\",\" \",\"启用\",\"平滑\",\"滚动\",\" */\\n\",\" \",\" background\",\":\",\" #\",\"1\",\"a\",\"1\",\"a\",\"1\",\"a\",\";\\n\",\" \",\" padding\",\":\",\" \",\"20\",\"px\",\";\\n\",\" \",\" border\",\"-radius\",\":\",\" \",\"8\",\"px\",\";\\n\",\"}\\n\\n\",\"/*\",\" \",\"单\",\"条\",\"消息\",\"动画\",\" */\",\"\\n\",\".message\",\"-entry\",\" {\\n\",\" \",\" animation\",\":\",\" slide\",\"In\",\" \",\"0\",\".\",\"3\",\"s\",\" ease\",\"-out\",\";\\n\",\" \",\" margin\",\"-bottom\",\":\",\" \",\"12\",\"px\",\";\\n\",\" \",\" color\",\":\",\" #\",\"c\",\"9\",\"d\",\"1\",\"d\",\"9\",\";\\n\",\" \",\" line\",\"-height\",\":\",\" \",\"1\",\".\",\"6\",\";\\n\",\"}\\n\\n\",\"@\",\"key\",\"frames\",\" slide\",\"In\",\" {\\n\",\" \",\" from\",\" {\\n\",\"   \",\" opacity\",\":\",\" \",\"0\",\";\\n\",\"   \",\" transform\",\":\",\" translate\",\"Y\",\"(\",\"20\",\"px\",\");\\n\",\" \",\" }\\n\",\" \",\" to\",\" {\\n\",\"   \",\" opacity\",\":\",\" \",\"1\",\";\\n\",\"   \",\" transform\",\":\",\" translate\",\"Y\",\"(\",\"0\",\");\\n\",\" \",\" }\\n\",\"}\\n\",\"</\",\"style\",\">\\n\\n\",\"<script\",\">\\n\",\"const\",\" output\",\"Container\",\" =\",\" document\",\".getElementById\",\"('\",\"g\",\"rok\",\"-output\",\"');\\n\",\"let\",\" is\",\"Sc\",\"rolling\",\" =\",\" false\",\";\\n\\n\",\"//\",\" \",\"1\",\".\",\" \",\"建立\",\" SSE\",\" \",\"连接\",\"\\n\",\"const\",\" event\",\"Source\",\" =\",\" new\",\" Event\",\"Source\",\"('/\",\"stream\",\"/g\",\"rok\",\"');\\n\\n\",\"//\",\" \",\"2\",\".\",\" \",\"处理\",\"数据\",\"到达\",\"\\n\",\"event\",\"Source\",\".on\",\"message\",\" =\",\" (\",\"event\",\")\",\" =>\",\" {\\n\",\" \",\" const\",\" message\",\" =\",\" JSON\",\".parse\",\"(event\",\".data\",\");\\n\",\" \",\" append\",\"Message\",\"(message\",\".content\",\");\\n\",\"};\\n\\n\",\"//\",\" \",\"3\",\".\",\" \",\"动态\",\"追加\",\"消息\",\"\\n\",\"function\",\" append\",\"Message\",\"(content\",\")\",\" {\\n\",\" \",\" const\",\" fragment\",\" =\",\" document\",\".create\",\"Document\",\"Fragment\",\"();\\n\",\" \",\" const\",\" entry\",\" =\",\" document\",\".createElement\",\"('\",\"div\",\"');\\n\",\"  \\n\",\" \",\" //\",\" \",\"逐\",\"字符\",\"动画\",\"\\n\",\" \",\" let\",\" char\",\"Index\",\" =\",\" \",\"0\",\";\\n\",\" \",\" const\",\" char\",\"Interval\",\" =\",\" set\",\"Interval\",\"(()\",\" =>\",\" {\\n\",\"   \",\" if\",\"(char\",\"Index\",\" >=\",\" content\",\".length\",\")\",\" {\\n\",\"     \",\" clear\",\"Interval\",\"(char\",\"Interval\",\");\\n\",\"     \",\" return\",\";\\n\",\"   \",\" }\\n\",\"   \",\" entry\",\".text\",\"Content\",\" +=\",\" content\",\".charAt\",\"(char\",\"Index\",\");\\n\",\"   \",\" char\",\"Index\",\"++;\\n\",\"   \",\" maintain\",\"Scroll\",\"();\",\" //\",\" \",\"维持\",\"滚动\",\"位置\",\"\\n\",\" \",\" },\",\" \",\"30\",\");\",\" //\",\" \",\"调整\",\"速度\",\"\\n  \\n\",\" \",\" entry\",\".class\",\"Name\",\" =\",\" '\",\"message\",\"-entry\",\"';\\n\",\" \",\" fragment\",\".appendChild\",\"(\",\"entry\",\");\\n\",\" \",\" output\",\"Container\",\".appendChild\",\"(f\",\"ragment\",\");\\n\",\"}\\n\\n\",\"//\",\" \",\"4\",\".\",\" \",\"智能\",\"滚动\",\"控制\",\"\\n\",\"function\",\" maintain\",\"Scroll\",\"()\",\" {\\n\",\" \",\" if\",\"(output\",\"Container\",\".sc\",\"roll\",\"Top\",\" >\",\" output\",\"Container\",\".sc\",\"roll\",\"Height\",\" -\",\" output\",\"Container\",\".client\",\"Height\",\" -\",\" \",\"100\",\")\",\" {\\n\",\"   \",\" request\",\"Animation\",\"Frame\",\"(()\",\" =>\",\" {\\n\",\"     \",\" output\",\"Container\",\".sc\",\"roll\",\"Top\",\" =\",\" output\",\"Container\",\".sc\",\"roll\",\"Height\",\";\\n\",\"   \",\" });\\n\",\" \",\" }\\n\",\"}\\n\\n\",\"//\",\" \",\"5\",\".\",\" \",\"错误\",\"处理\",\"\\n\",\"event\",\"Source\",\".on\",\"error\",\" =\",\" ()\",\" =>\",\" {\\n\",\" \",\" console\",\".error\",\"('\",\"Stream\",\" connection\",\" lost\",\"');\\n\",\" \",\" event\",\"Source\",\".close\",\"();\\n\",\"};\\n\",\"</\",\"script\",\">\\n\",\"```\\n\\n\",\"---\\n\\n\",\"###\",\" \",\"三\",\"、\",\"核心\",\"实现\",\"逻辑\",\"分解\",\"\\n\\n\",\"1\",\".\",\" **\",\"流\",\"式\",\"连接\",\"建立\",\"**\\n\",\"```\",\"javascript\",\"\\n\",\"const\",\" event\",\"Source\",\" =\",\" new\",\" Event\",\"Source\",\"('/\",\"stream\",\"/g\",\"rok\",\"');\\n\",\"```\\n\",\"-\",\" \",\"创建\",\" SSE\",\" \",\"连接\",\"通道\",\"，\",\"服务\",\"端\",\"需\",\"保持\",\"长\",\"连接\",\"并\",\"定期\",\"发送\",\"`\",\"data\",\":`\",\"消息\",\"\\n\\n\",\"2\",\".\",\" **\",\"动态\",\"内容\",\"渲染\",\"**\\n\",\"```\",\"javascript\",\"\\n\",\"//\",\" \",\"使用\",\"文档\",\"片段\",\"优化\",\"渲染\",\"性能\",\"\\n\",\"const\",\" fragment\",\" =\",\" document\",\".create\",\"Document\",\"Fragment\",\"();\\n\\n\",\"//\",\" \",\"逐\",\"字符\",\"动画\",\"通过\",\"间隔\",\"定时\",\"器\",\"实现\",\"\\n\",\"let\",\" char\",\"Index\",\" =\",\" \",\"0\",\";\\n\",\"const\",\" char\",\"Interval\",\" =\",\" set\",\"Interval\",\"(()\",\" =>\",\" {\\n\",\" \",\" entry\",\".text\",\"Content\",\" +=\",\" content\",\".charAt\",\"(char\",\"Index\",\"++\",\");\\n\",\"},\",\" \",\"30\",\");\\n\",\"```\\n\",\"-\",\" \",\"使用\",\"`\",\"Document\",\"Fragment\",\"`\",\"减少\",\"重\",\"绘\",\"次数\",\"\\n\",\"-\",\" \",\"30\",\"ms\",\" \",\"间隔\",\"产生\",\"打字\",\"机\",\"效果\",\"（\",\"可根据\",\"需要\",\"调整\",\"）\\n\\n\",\"3\",\".\",\" **\",\"智能\",\"滚动\",\"策略\",\"**\\n\",\"```\",\"javascript\",\"\\n\",\"function\",\" maintain\",\"Scroll\",\"()\",\" {\\n\",\" \",\" const\",\" threshold\",\" =\",\" \",\"100\",\";\",\" //\",\" \",\"像素\",\"缓冲\",\"区间\",\"\\n\",\" \",\" const\",\" from\",\"Bottom\",\" =\",\" output\",\"Container\",\".sc\",\"roll\",\"Height\",\" -\",\" \\n\",\"                   \",\" output\",\"Container\",\".sc\",\"roll\",\"Top\",\" -\",\" \\n\",\"                   \",\" output\",\"Container\",\".client\",\"Height\",\";\\n\\n\",\" \",\" if\",\"(from\",\"Bottom\",\" <\",\" threshold\",\")\",\" {\\n\",\"   \",\" request\",\"Animation\",\"Frame\",\"(()\",\" =>\",\" {\\n\",\"     \",\" output\",\"Container\",\".sc\",\"roll\",\"Top\",\" =\",\" output\",\"Container\",\".sc\",\"roll\",\"Height\",\";\\n\",\"   \",\" });\\n\",\" \",\" }\\n\",\"}\\n\",\"```\\n\",\"-\",\" \",\"仅在\",\"用户\",\"未\",\"手动\",\"滚动\",\"时\",\"维持\",\"自动\",\"滚动\",\"\\n\",\"-\",\" \",\"使用\",\"`\",\"request\",\"Animation\",\"Frame\",\"`\",\"优化\",\"滚动\",\"性能\",\"\\n\\n\",\"4\",\".\",\" **\",\"动画\",\"效果\",\"实现\",\"**\\n\",\"```\",\"css\",\"\\n\",\"/*\",\" \",\"入场\",\"动画\",\" */\\n\",\"@\",\"key\",\"frames\",\" slide\",\"In\",\" {\\n\",\" \",\" from\",\" {\",\" opacity\",\":\",\" \",\"0\",\";\",\" transform\",\":\",\" translate\",\"Y\",\"(\",\"20\",\"px\",\");\",\" }\\n\",\" \",\" to\",\" {\",\" opacity\",\":\",\" \",\"1\",\";\",\" transform\",\":\",\" translate\",\"Y\",\"(\",\"0\",\");\",\" }\\n\",\"}\\n\\n\",\"/*\",\" \",\"平滑\",\"滚动\",\" */\\n\",\".stream\",\"-container\",\" {\\n\",\" \",\" scroll\",\"-be\",\"havior\",\":\",\" smooth\",\";\\n\",\" \",\" scroll\",\"-p\",\"adding\",\"-bottom\",\":\",\" \",\"20\",\"px\",\";\",\" /*\",\" \",\"预留\",\"滚动\",\"空间\",\" */\\n\",\"}\\n\",\"```\\n\\n\",\"---\\n\\n\",\"###\",\" \",\"四\",\"、\",\"性能\",\"优化\",\"技巧\",\"\\n\\n\",\"1\",\".\",\" **\",\"渲染\",\"优化\",\"**\\n\",\"-\",\" \",\"使用\",\"`\",\"will\",\"-change\",\":\",\" transform\",\"`\",\"提升\",\"动画\",\"元素\",\"性能\",\"\\n\",\"-\",\" \",\"对\",\"长\",\"内容\",\"进行\",\"分\",\"块\",\"渲染\",\"（\",\"每\",\" \",\"50\",\" \",\"行\",\"创建一个\",\"新\",\"片段\",\"）\\n\\n\",\"2\",\".\",\" **\",\"内存\",\"管理\",\"**\\n\",\"```\",\"javascript\",\"\\n\",\"//\",\" \",\"自动\",\"清理\",\"历史\",\"内容\",\"\\n\",\"const\",\" MAX\",\"_L\",\"INES\",\" =\",\" \",\"200\",\";\\n\",\"if\",\"(output\",\"Container\",\".children\",\".length\",\" >\",\" MAX\",\"_L\",\"INES\",\")\",\" {\\n\",\" \",\" output\",\"Container\",\".remove\",\"Child\",\"(output\",\"Container\",\".first\",\"Child\",\");\\n\",\"}\\n\",\"```\\n\\n\",\"3\",\".\",\" **\",\"网络\",\"优化\",\"**\\n\",\"-\",\" \",\"服务\",\"端\",\"启用\",\" g\",\"zip\",\" \",\"压缩\",\"\\n\",\"-\",\" \",\"设置\",\"合适的\",\" SSE\",\" \",\"重\",\"连\",\"超\",\"时\",\"（\",\"默认\",\" \",\"3\",\" \",\"秒\",\"）\\n\\n\",\"---\\n\\n\",\"###\",\" \",\"五\",\"、\",\"扩展\",\"能力\",\"\\n\\n\",\"1\",\".\",\" **\",\"交互\",\"增强\",\"**\\n\",\"```\",\"javascript\",\"\\n\",\"//\",\" \",\"允许\",\"暂停\",\"滚动\",\"\\n\",\"let\",\" auto\",\"Scroll\",\" =\",\" true\",\";\\n\\n\",\"output\",\"Container\",\".addEventListener\",\"('\",\"scroll\",\"',\",\" ()\",\" =>\",\" {\\n\",\" \",\" const\",\" from\",\"Bottom\",\" =\",\" output\",\"Container\",\".sc\",\"roll\",\"Height\",\" -\",\" \\n\",\"                   \",\" output\",\"Container\",\".sc\",\"roll\",\"Top\",\" -\",\" \\n\",\"                   \",\" output\",\"Container\",\".client\",\"Height\",\";\\n\",\" \",\" auto\",\"Scroll\",\" =\",\" from\",\"Bottom\",\" <=\",\" \",\"100\",\";\\n\",\"});\\n\",\"```\\n\\n\",\"2\",\".\",\" **\",\"可视化\",\"增强\",\"**\\n\",\"```\",\"css\",\"\\n\",\"/*\",\" \",\"添加\",\"时间\",\"轴\",\" */\\n\",\".message\",\"-entry\",\"::\",\"before\",\" {\\n\",\" \",\" content\",\":\",\" '';\\n\",\" \",\" display\",\":\",\" inline\",\"-block\",\";\\n\",\" \",\" width\",\":\",\" \",\"8\",\"px\",\";\\n\",\" \",\" height\",\":\",\" \",\"8\",\"px\",\";\\n\",\" \",\" background\",\":\",\" #\",\"58\",\"a\",\"6\",\"ff\",\";\\n\",\" \",\" border\",\"-radius\",\":\",\" \",\"50\",\"%;\\n\",\" \",\" margin\",\"-right\",\":\",\" \",\"12\",\"px\",\";\\n\",\"}\\n\",\"```\\n\\n\",\"---\\n\\n\",\"这种\",\"实现\",\"方案\",\"在\",\" \",\"1\",\"MB\",\"/s\",\" \",\"的数据\",\"流\",\"下\",\"可实现\",\"约\",\" \",\"20\",\"f\",\"ps\",\" \",\"的\",\"稳定\",\"渲染\",\"，\",\"通过\",\"多\",\"级\",\"动画\",\"策略\",\"兼顾\",\"了\",\"视觉效果\",\"与\",\"性能\",\"表现\",\"。\",\"实际\",\"部署\",\"时\",\"建议\",\"配合\",\" Web\",\" Workers\",\" \",\"进行\",\"数据处理\",\"，\",\"主\",\"线程\",\"专注\",\"渲染\",\"。\",\"\"]";
            // 对text进行分词，需要把换行符也分出来
            List<String> lemmas = JSONArray.parseArray(textArray, String.class);
//            System.out.println(CollUtil.join(lemmas, ""));
            for (String lemma : lemmas) {
                // 转义换行符（保留真正的消息结束符 \n\n）
//                String escaped = lemma.replace("\n", "\\n");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", lemma);

                // 构建符合 SSE 格式的数据包
//                String sseFormatted = "data: " + escaped + "\n\n";
                try {
                    emitter.send(SseEmitter.event()
//                            .name("message")
                            .data(jsonObject)
                            .build());
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("content", "Stream completed");
                emitter.send(SseEmitter.event()
                        .name("complete")
                        .data(jsonObject)
                        .build());
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }, asyncExecutor);
        return emitter;
    }

    /**
     * 获取默认的AI模型配置
     *
     * @return AI模型配置
     */
    private LlmConfig getDefaultModel(Long llmId) {
        // 1. 查询启用的模型配置
        LambdaQueryWrapper<LlmConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LlmConfig::getEnabled, true);
        if (null != llmId) {
            wrapper.eq(LlmConfig::getId, llmId);
        }
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
        requestBody.put("model", model.getModelCode());
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
        List<Map<String, Object>> choicesList = (List<Map<String, Object>>) response.get(choicesCol);
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
     * @param requestId
     * @param isStop
     */
    private void saveChatHistory(String sessionId, Long[] kbIds, String question, String answer, Long modelId,
                                 long processTime, String requestId, boolean isStop) {
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
        history.setRequestId(requestId);
        history.setIsStop(isStop ? 1 : 0);
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
        requestBody.put("model", model.getModelCode());
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put(contentCol, "");
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put(contentCol, prompt);
        switch (model.getModelProvider()) {
            case "ollama":
                requestBody.put("prompt", prompt);
                requestBody.put("stream", true);
                break;
            default:
                requestBody.put("messages",
                        new JSONObject[]{systemMessage, userMessage});
                requestBody.put("temperature", 0.7);
                requestBody.put("max_tokens", 2000);
                requestBody.put("stream", true);
                break;
        }
        return requestBody;
    }
}
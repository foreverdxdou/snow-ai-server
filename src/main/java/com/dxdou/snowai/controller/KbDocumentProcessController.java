package com.dxdou.snowai.controller;

import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.DocumentProcessRequest;
import com.dxdou.snowai.domain.model.DocumentProcessResult;
import com.dxdou.snowai.service.KbDocumentProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 知识库文档处理控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库文档处理")
@RestController
@RequestMapping("/api/v1/kb/document/process")
@RequiredArgsConstructor
public class KbDocumentProcessController {

    private final KbDocumentProcessService documentProcessService;

    @Operation(summary = "生成文档摘要")
    @PostMapping("/summary")
    public R<String> generateSummary(@RequestBody DocumentProcessRequest request) {
        return R.ok(documentProcessService.generateSummary(request.getContent(), request.getMaxLength()));
    }

    @Operation(summary = "提取文档关键词")
    @PostMapping("/keywords")
    public R<String> extractKeywords(@RequestBody DocumentProcessRequest request) {
        return R.ok(documentProcessService.extractKeywords(request.getContent(), request.getMaxKeywords()));
    }

    @Operation(summary = "处理文档内容")
    @PostMapping("/process")
    public R<DocumentProcessResult> processDocument(@RequestBody DocumentProcessRequest request) {
        return R.ok(documentProcessService.processDocument(request.getContent(), request.getMaxSummaryLength(), request.getMaxKeywords()));
    }
}
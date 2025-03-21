package com.dxdou.snowai.controller;

import com.dxdou.snowai.domain.model.DocumentProcessResult;
import com.dxdou.snowai.service.KbDocumentProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public String generateSummary(
            @Parameter(description = "文档内容") @RequestParam String content,
            @Parameter(description = "摘要最大长度") @RequestParam(defaultValue = "200") int maxLength) {
        return documentProcessService.generateSummary(content, maxLength);
    }

    @Operation(summary = "提取文档关键词")
    @PostMapping("/keywords")
    public String extractKeywords(
            @Parameter(description = "文档内容") @RequestParam String content,
            @Parameter(description = "关键词最大数量") @RequestParam(defaultValue = "10") int maxKeywords) {
        return documentProcessService.extractKeywords(content, maxKeywords);
    }

    @Operation(summary = "处理文档内容")
    @PostMapping("/process")
    public DocumentProcessResult processDocument(
            @Parameter(description = "文档内容") @RequestParam String content,
            @Parameter(description = "摘要最大长度") @RequestParam(defaultValue = "200") int maxSummaryLength,
            @Parameter(description = "关键词最大数量") @RequestParam(defaultValue = "10") int maxKeywords) {
        return documentProcessService.processDocument(content, maxSummaryLength, maxKeywords);
    }
}
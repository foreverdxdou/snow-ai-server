package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.vo.KbSearchVO;
import com.dxdou.snowai.service.KbSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识库智能搜索控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库智能搜索")
@RestController
@RequestMapping("/api/v1/kb/search")
@RequiredArgsConstructor
public class KbSearchController {

    private final KbSearchService searchService;

    @Operation(summary = "语义搜索")
    @GetMapping("/semantic")
    public R<Page<KbSearchVO>> semanticSearch(
            @Parameter(description = "搜索关键词") @RequestParam String query,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "标签ID列表") @RequestParam(required = false) List<Long> tagIds) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(searchService.semanticSearch(query, kbId, page, tagIds));
    }

    @Operation(summary = "关键词搜索")
    @GetMapping("/keyword")
    public R<Page<KbSearchVO>> keywordSearch(
            @Parameter(description = "搜索关键词") @RequestParam String query,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "标签ID列表") @RequestParam(required = false) List<Long> tagIds) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(searchService.keywordSearch(query, kbId, page, tagIds));
    }

    @Operation(summary = "混合搜索")
    @GetMapping("/hybrid")
    public R<Page<KbSearchVO>> hybridSearch(
            @Parameter(description = "搜索关键词") @RequestParam String query,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size,
            @Parameter(description = "标签ID列表") @RequestParam(required = false) List<Long> tagIds) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(searchService.hybridSearch(query, kbId, page, tagIds));
    }

    @Operation(summary = "获取文档相似度")
    @GetMapping("/similarity")
    public R<Double> getDocumentSimilarity(
            @Parameter(description = "文档1 ID") @RequestParam Long docId1,
            @Parameter(description = "文档2 ID") @RequestParam Long docId2) {
        return R.ok(searchService.getDocumentSimilarity(docId1, docId2));
    }
}
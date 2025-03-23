package com.dxdou.snowai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.dto.UserBehaviorRequest;
import com.dxdou.snowai.domain.vo.KbSearchVO;
import com.dxdou.snowai.service.KbRecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 知识库个性化推荐控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "知识库个性化推荐")
@RestController
@RequestMapping("/api/v1/kb/recommend")
@RequiredArgsConstructor
public class KbRecommendController {

    private final KbRecommendService recommendService;

    @Operation(summary = "获取个性化推荐")
    @GetMapping("/personal")
    public R<Page<KbSearchVO>> getPersonalRecommendations(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(recommendService.getPersonalRecommendations(userId, kbId, page));
    }

    @Operation(summary = "获取热门推荐")
    @GetMapping("/hot")
    public R<Page<KbSearchVO>> getHotRecommendations(
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(recommendService.getHotRecommendations(kbId, page));
    }

    @Operation(summary = "获取相关推荐")
    @GetMapping("/related")
    public R<Page<KbSearchVO>> getRelatedRecommendations(
            @Parameter(description = "文档ID") @RequestParam Long docId,
            @Parameter(description = "知识库ID") @RequestParam Long kbId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") long size) {
        Page<KbSearchVO> page = new Page<>(current, size);
        return R.ok(recommendService.getRelatedRecommendations(docId, kbId, page));
    }

    @Operation(summary = "记录用户行为")
    @PostMapping("/behavior")
    public R<Void> recordUserBehavior(@RequestBody UserBehaviorRequest request) {
        recommendService.recordUserBehavior(request.getUserId(), request.getDocId(), request.getBehaviorType());
        return R.ok(null);
    }
}
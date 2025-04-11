package com.dxdou.snowai.controller;

import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.vo.HomeStatsVO;
import com.dxdou.snowai.service.KbDocumentService;
import com.dxdou.snowai.service.KbKnowledgeBaseService;
import com.dxdou.snowai.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "首页管理")
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final KbKnowledgeBaseService kbService;
    private final KbDocumentService docService;
    private final SysUserService userService;

    @Operation(summary = "获取首页统计信息")
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('home:view')")
    public R<HomeStatsVO> getHomeStats() {
        HomeStatsVO stats = new HomeStatsVO();

        // 设置知识库统计
        HomeStatsVO.KbStats kbStats = new HomeStatsVO.KbStats();
        kbStats.setTotalCount(kbService.count());
        kbStats.setActiveCount(kbService.countByStatus(1));
        kbStats.setWeeklyNewCount(kbService.countByCreateTimeAfter(LocalDate.now().minusWeeks(1).atStartOfDay()));
        kbStats.setMonthlyNewCount(kbService
                .countByCreateTimeAfter(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()));
        stats.setKbStats(kbStats);

        // 设置文档统计
        HomeStatsVO.DocStats docStats = new HomeStatsVO.DocStats();
        docStats.setTotalCount(docService.count());
        docStats.setParsedCount(docService.countByParseStatus(1));
        docStats.setWeeklyNewCount(docService.countByCreateTimeAfter(LocalDate.now().minusWeeks(1).atStartOfDay()));
        docStats.setMonthlyNewCount(docService
                .countByCreateTimeAfter(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay()));
        stats.setDocStats(docStats);

        // 设置最新知识库
        List<HomeStatsVO.LatestKb> latestKbs = kbService.getLatestKbs(5).stream()
                .map(kb -> {
                    HomeStatsVO.LatestKb latestKb = new HomeStatsVO.LatestKb();
                    latestKb.setId(kb.getId());
                    latestKb.setName(kb.getName());
                    latestKb.setDescription(kb.getDescription());
                    latestKb.setCreateTime(kb.getCreateTime().toString());
                    latestKb.setCreatorName(userService.getById(kb.getCreatorId()).getNickname());
                    return latestKb;
                })
                .collect(Collectors.toList());
        stats.setLatestKbs(latestKbs);

        // 设置最新文档
        List<HomeStatsVO.LatestDoc> latestDocs = docService.getLatestDocs(5).stream()
                .map(doc -> {
                    HomeStatsVO.LatestDoc latestDoc = new HomeStatsVO.LatestDoc();
                    latestDoc.setId(doc.getId());
                    latestDoc.setTitle(doc.getTitle());
                    latestDoc.setFileType(doc.getFileType());
                    latestDoc.setKbName(kbService.getById(doc.getKbId()).getName());
                    latestDoc.setCreateTime(doc.getCreateTime().toString());
                    latestDoc.setCreatorName(userService.getById(doc.getCreatorId()).getNickname());
                    return latestDoc;
                })
                .collect(Collectors.toList());
        stats.setLatestDocs(latestDocs);

        return R.ok(stats);
    }

    @Operation(summary = "获取知识库增长趋势")
    @GetMapping("/kb/trend")
    @PreAuthorize("hasAuthority('home:view')")
    public R<List<Long>> getKbTrend(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return R.ok(kbService.getKbTrend(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
    }

    @Operation(summary = "获取文档增长趋势")
    @GetMapping("/doc/trend")
    @PreAuthorize("hasAuthority('home:view')")
    public R<List<Long>> getDocTrend(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return R.ok(docService.getDocTrend(startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
    }
}
package com.dxdou.snowai.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 首页统计信息VO类
 *
 * @author foreverdxdou
 */
@Data
@Schema(description = "首页统计信息")
public class HomeStatsVO {

    /**
     * 知识库统计
     */
    @Schema(description = "知识库统计")
    private KbStats kbStats;

    /**
     * 文档统计
     */
    @Schema(description = "文档统计")
    private DocStats docStats;

    /**
     * 最新知识库
     */
    @Schema(description = "最新知识库列表")
    private List<LatestKb> latestKbs;

    /**
     * 最新文档
     */
    @Schema(description = "最新文档列表")
    private List<LatestDoc> latestDocs;

    @Data
    @Schema(description = "知识库统计信息")
    public static class KbStats {
        @Schema(description = "知识库总数")
        private Long totalCount;

        @Schema(description = "活跃知识库数")
        private Long activeCount;

        @Schema(description = "本周新增数")
        private Long weeklyNewCount;

        @Schema(description = "本月新增数")
        private Long monthlyNewCount;
    }

    @Data
    @Schema(description = "文档统计信息")
    public static class DocStats {
        @Schema(description = "文档总数")
        private Long totalCount;

        @Schema(description = "已解析文档数")
        private Long parsedCount;

        @Schema(description = "本周新增数")
        private Long weeklyNewCount;

        @Schema(description = "本月新增数")
        private Long monthlyNewCount;
    }

    @Data
    @Schema(description = "最新知识库信息")
    public static class LatestKb {
        @Schema(description = "知识库ID")
        private Long id;

        @Schema(description = "知识库名称")
        private String name;

        @Schema(description = "知识库描述")
        private String description;

        @Schema(description = "创建时间")
        private String createTime;

        @Schema(description = "创建者名称")
        private String creatorName;
    }

    @Data
    @Schema(description = "最新文档信息")
    public static class LatestDoc {
        @Schema(description = "文档ID")
        private Long id;

        @Schema(description = "文档标题")
        private String title;

        @Schema(description = "文档类型")
        private String fileType;

        @Schema(description = "知识库名称")
        private String kbName;

        @Schema(description = "创建时间")
        private String createTime;

        @Schema(description = "创建者名称")
        private String creatorName;
    }
}
package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库用户行为实体
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_user_behavior")
public class KbUserBehavior {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 文档ID
     */
    private Long docId;

    /**
     * 行为类型（1:查看 2:搜索 3:下载）
     */
    private String behaviorType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
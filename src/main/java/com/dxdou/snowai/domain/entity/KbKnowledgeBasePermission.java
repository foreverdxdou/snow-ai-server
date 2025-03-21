package com.dxdou.snowai.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 知识库权限实体类
 *
 * @author foreverdxdou
 */
@Data
@TableName("kb_knowledge_base_permission")
public class KbKnowledgeBasePermission {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 知识库ID
     */
    private Long kbId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限类型（1：查看，2：编辑，3：管理）
     */
    private Integer permissionType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
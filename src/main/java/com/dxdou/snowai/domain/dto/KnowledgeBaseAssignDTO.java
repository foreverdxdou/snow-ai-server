package com.dxdou.snowai.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "分配知识库权限请求参数")
public class KnowledgeBaseAssignDTO {

    @Schema(description = "用户ID列表")
    private List<Long> userIds;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "权限类型")
    private Integer permissionType;
}
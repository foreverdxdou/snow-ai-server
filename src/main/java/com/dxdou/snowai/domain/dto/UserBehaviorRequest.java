package com.dxdou.snowai.domain.dto;

import lombok.Data;

@Data
public class UserBehaviorRequest {
    private Long userId;
    private Long docId;
    private String behaviorType;
} 
package com.dxdou.snowai.domain.dto;

import lombok.Data;

@Data
public class DocumentProcessRequest {
    private String content;
    private int maxLength = 200;
    private int maxKeywords = 10;
    private int maxSummaryLength = 200;
}
package com.dxdou.snowai.domain.dto;

import lombok.Data;

@Data
public class KbCategoryUpdateDTO {
    private String name;
    private String description;
    private Long parentId;
    private Integer sort;
} 
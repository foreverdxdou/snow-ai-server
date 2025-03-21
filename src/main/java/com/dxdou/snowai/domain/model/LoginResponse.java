package com.dxdou.snowai.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录响应模型
 *
 * @author foreverdxdou
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    /**
     * JWT令牌
     */
    private String token;
}
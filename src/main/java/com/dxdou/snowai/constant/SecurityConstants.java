package com.dxdou.snowai.constant;

public class SecurityConstants {
    public static final String[] EXCLUDED_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/public/**",
            "/error",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"};
} 
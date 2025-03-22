package com.dxdou.snowai.constant;

public class SecurityConstants {
    public static final String[] EXCLUDED_URLS = {"/api/auth/login",
            "/api/auth/register",
            "/api/public/**",
            "/error",
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"};
} 
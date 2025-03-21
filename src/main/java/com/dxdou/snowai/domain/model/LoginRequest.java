package com.dxdou.snowai.domain.model;

import lombok.Data;

/**
 * 登录请求模型
 *
 * @author foreverdxdou
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
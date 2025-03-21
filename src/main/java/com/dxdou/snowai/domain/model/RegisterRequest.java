package com.dxdou.snowai.domain.model;

import lombok.Data;

/**
 * 注册请求模型
 *
 * @author foreverdxdou
 */
@Data
public class RegisterRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String email;
}
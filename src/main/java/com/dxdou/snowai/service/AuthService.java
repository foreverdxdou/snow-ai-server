package com.dxdou.snowai.service;

import com.dxdou.snowai.domain.entity.SysUser;

/**
 * 认证服务接口
 *
 * @author foreverdxdou
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT令牌
     */
    String login(String username, String password);

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    void register(SysUser user);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    SysUser getCurrentUser();
}
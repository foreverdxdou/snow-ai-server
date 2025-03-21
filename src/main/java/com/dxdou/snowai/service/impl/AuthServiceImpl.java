package com.dxdou.snowai.service.impl;

import com.dxdou.snowai.config.JwtConfig;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.service.AuthService;
import com.dxdou.snowai.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        // 进行身份认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // 设置认证信息到Security上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        return jwtConfig.generateToken(username);
    }

    @Override
    public void register(SysUser user) {
        // 检查用户名是否已存在
        if (sysUserService.getByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 创建用户（默认分配普通用户角色）
        sysUserService.createUser(user, new Long[] { 2L });
    }

    @Override
    public void logout() {
        // 清除Security上下文
        SecurityContextHolder.clearContext();
    }

    @Override
    public SysUser getCurrentUser() {
        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("用户未登录");
        }

        // 获取用户名
        String username = authentication.getName();

        // 获取用户信息
        return sysUserService.getByUsername(username);
    }
}
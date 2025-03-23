package com.dxdou.snowai.controller;

import com.dxdou.snowai.common.R;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.domain.model.LoginRequest;
import com.dxdou.snowai.domain.model.LoginResponse;
import com.dxdou.snowai.domain.model.RegisterRequest;
import com.dxdou.snowai.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author foreverdxdou
 */
@Tag(name = "认证管理", description = "认证相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return R.ok(new LoginResponse(token));
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<Void> register(@RequestBody RegisterRequest request) {
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        authService.register(user);
        return R.ok(null);
    }

    /**
     * 退出登录
     *
     * @return 退出结果
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout() {
        authService.logout();
        return R.ok(null);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/current-user")
    public R<SysUser> getCurrentUser() {
        return R.ok(authService.getCurrentUser());
    }
}
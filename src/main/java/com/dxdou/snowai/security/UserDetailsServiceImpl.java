package com.dxdou.snowai.security;

import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户认证服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        SysUser user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }else if (user.getStatus() == 0) {
            throw new DisabledException("用户已被禁用");
        } else if (user.getStatus() == 2) {
            throw new LockedException("用户已被锁定");
        } else if (user.getStatus() == 3) {
            throw new AccountExpiredException("用户账户已过期");
        } else if (user.getStatus() == 4) {
            throw new CredentialsExpiredException("用户密码已过期");
        }

        // 查询用户角色
        List<String> roleCodes = userService.getRolesByUserId(user.getId())
                .stream()
                .map(role -> "ROLE_" + role.getRoleCode())
                .collect(Collectors.toList());

        // 查询用户权限
        List<String> permissions = userService.getUserPermissions(user.getId())
                .stream()
                .map(permission -> permission.getPermissionCode())
                .collect(Collectors.toList());

        // 合并角色和权限
        List<SimpleGrantedAuthority> authorities = roleCodes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
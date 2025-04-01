package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.domain.entity.SysUserRole;
import com.dxdou.snowai.domain.vo.SysUserVO;
import com.dxdou.snowai.mapper.SysUserMapper;
import com.dxdou.snowai.mapper.SysUserRoleMapper;
import com.dxdou.snowai.service.SysPermissionService;
import com.dxdou.snowai.service.SysRoleService;
import com.dxdou.snowai.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleService roleService;
    private final SysPermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public Page<SysUserVO> getUserPage(Page<SysUser> page, String username, Integer status, Long deptId, String email, String phone, String nickName) {
        return userMapper.selectUserList(page, username, status, deptId, email, phone, nickName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUser user, Long[] roleIds) {
        // 1. 检查用户名是否已存在
        if (userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, user.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. 保存用户
        userMapper.insert(user);

        // 4. 保存用户角色关联
        if (roleIds != null && roleIds.length > 0) {
            userRoleMapper.insertBatch(Arrays.stream(roleIds)
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(user.getId());
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user, Long[] roleIds) {
        // 1. 检查用户是否存在
        SysUser existingUser = getById(user.getId());
        if (existingUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 检查用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername()) &&
                userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, user.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 3. 更新用户信息
        userMapper.updateById(user);

        // 4. 更新用户角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, user.getId()));
        if (roleIds != null && roleIds.length > 0) {
            userRoleMapper.insertBatch(Arrays.stream(roleIds)
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(user.getId());
                        userRole.setRoleId(roleId);
                        return userRole;
                    })
                    .collect(Collectors.toList()));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 1. 删除用户
        userMapper.deleteById(userId);

        // 2. 删除用户角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
    }

    @Override
    public void resetPassword(Long userId, String password) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(password));
        updateById(user);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    @Override
    public void updateUserStatus(Long userId, Integer status) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        updateById(user);
    }

    @Override
    public void updateUserAvatar(Long userId, String avatar) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setAvatar(avatar);
        updateById(user);
    }

    @Override
    public void updateUserInfo(Long userId, String nickname, String email) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setNickname(nickname);
        user.setEmail(email);
        updateById(user);
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return roleService.getRolesByUserId(userId);
    }

    @Override
    public List<SysPermission> getUserPermissions(Long userId) {
        List<SysRole> roles = getRolesByUserId(userId);
        List<Long> roleIds = roles.stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        return permissionService.getPermissionsByRoleIds(roleIds);
    }
}
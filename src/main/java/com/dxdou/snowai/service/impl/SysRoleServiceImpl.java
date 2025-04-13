package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.entity.SysRolePermission;
import com.dxdou.snowai.domain.vo.SysRoleVO;
import com.dxdou.snowai.mapper.SysRoleMapper;
import com.dxdou.snowai.mapper.SysRolePermissionMapper;
import com.dxdou.snowai.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 系统角色服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper roleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Override
    public Page<SysRoleVO> getRolePage(Page<SysRole> page, String roleName, String roleCode) {
        return roleMapper.selectRoleList(page, roleName, roleCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRole role, Long[] permissionIds) {
        // 检查角色编码是否已存在
        if (roleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, role.getRoleCode())) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 保存角色
        roleMapper.insert(role);

        // 保存角色权限关联
        if (permissionIds != null && permissionIds.length > 0) {
            List<SysRolePermission> rolePermissions = Arrays.stream(permissionIds).distinct()
                    .map(permissionId -> {
                        SysRolePermission rolePermission = new SysRolePermission();
                        rolePermission.setRoleId(role.getId());
                        rolePermission.setPermissionId(permissionId);
                        return rolePermission;
                    })
                    .toList();
            rolePermissionMapper.insertBatch(rolePermissions);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role, Long[] permissionIds) {
        // 检查角色编码是否已存在
        if (roleMapper.selectCount(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleCode, role.getRoleCode())
                .ne(SysRole::getId, role.getId())) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 更新角色
        roleMapper.updateById(role);

        // 删除原有权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, role.getId()));

        // 保存新的权限关联
        if (permissionIds != null && permissionIds.length > 0) {
            List<SysRolePermission> rolePermissions = Arrays.stream(permissionIds).distinct()
                    .map(permissionId -> {
                        SysRolePermission rolePermission = new SysRolePermission();
                        rolePermission.setRoleId(role.getId());
                        rolePermission.setPermissionId(permissionId);
                        return rolePermission;
                    })
                    .toList();
            rolePermissionMapper.insertBatch(rolePermissions);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        // 删除角色
        roleMapper.deleteById(roleId);

        // 删除角色权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId));
    }

    @Override
    public List<SysPermission> getRolePermissions(Long roleId) {
        return roleMapper.selectRolePermissions(roleId);
    }

    @Override
    public SysRole getByRoleCode(String roleCode) {
        return roleMapper.selectByRoleCode(roleCode);
    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public void updateRoleStatus(Long roleId, Integer status) {
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setStatus(status);
        roleMapper.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolePermissions(Long roleId, Long[] permissionIds) {
        // 删除原有权限关联
        rolePermissionMapper.delete(new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId));

        // 保存新的权限关联
        if (permissionIds != null && permissionIds.length > 0) {
            List<SysRolePermission> rolePermissions = Arrays.stream(permissionIds)
                    .map(permissionId -> {
                        SysRolePermission rolePermission = new SysRolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        return rolePermission;
                    })
                    .toList();
            rolePermissionMapper.insertBatch(rolePermissions);
        }
    }
}
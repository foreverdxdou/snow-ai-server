package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.vo.SysPermissionVO;
import com.dxdou.snowai.mapper.SysPermissionMapper;
import com.dxdou.snowai.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统权限服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission>
        implements SysPermissionService {

    private final SysPermissionMapper permissionMapper;

    @Override
    public List<SysPermissionVO> getPermissionTree(Long parentId, Integer type, Integer status, String name) {
        return permissionMapper.selectPermissionTree(parentId, type, status, name);
    }

    @Override
    public List<SysPermission> getUserPermissions(Long userId) {
        return permissionMapper.selectUserPermissions(userId);
    }

    @Override
    public List<SysPermission> getPermissionsByRoleIds(List<Long> roleIds) {
        return permissionMapper.selectPermissionsByRoleIds(roleIds);
    }
}
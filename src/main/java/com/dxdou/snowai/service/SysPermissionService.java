package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.vo.SysPermissionVO;

import java.util.List;

/**
 * 系统权限服务接口
 *
 * @author foreverdxdou
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 查询权限树
     *
     * @param parentId 父权限ID
     * @param type     权限类型
     * @param status   状态
     * @return 权限树
     */
    List<SysPermissionVO> getPermissionTree(Long parentId, Integer type, Integer status);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> getUserPermissions(Long userId);

    /**
     * 根据角色ID列表查询权限列表
     *
     * @param roleIds 角色ID列表
     * @return 权限列表
     */
    List<SysPermission> getPermissionsByRoleIds(List<Long> roleIds);

}
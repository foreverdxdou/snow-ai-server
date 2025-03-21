package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.vo.SysRoleVO;

import java.util.List;

/**
 * 系统角色服务接口
 *
 * @author foreverdxdou
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page     分页参数
     * @param roleName 角色名称
     * @param roleCode 角色编码
     * @return 角色列表
     */
    Page<SysRoleVO> getRolePage(Page<SysRole> page, String roleName, String roleCode);

    /**
     * 创建角色
     *
     * @param role          角色信息
     * @param permissionIds 权限ID列表
     */
    void createRole(SysRole role, Long[] permissionIds);

    /**
     * 更新角色
     *
     * @param role          角色信息
     * @param permissionIds 权限ID列表
     */
    void updateRole(SysRole role, Long[] permissionIds);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> getRolePermissions(Long roleId);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRole getByRoleCode(String roleCode);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 更新角色状态
     *
     * @param roleId 角色ID
     * @param status 状态
     */
    void updateRoleStatus(Long roleId, Integer status);

    /**
     * 分配角色权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    void assignRolePermissions(Long roleId, Long[] permissionIds);

}
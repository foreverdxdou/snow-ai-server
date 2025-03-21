package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.vo.SysRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page     分页参数
     * @param roleName 角色名称
     * @param roleCode 角色编码
     * @return 角色列表
     */
    Page<SysRoleVO> selectRoleList(Page<SysRole> page, @Param("roleName") String roleName,
            @Param("roleCode") String roleCode);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> selectRolePermissions(@Param("roleId") Long roleId);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(@Param("userId") Long userId);

}
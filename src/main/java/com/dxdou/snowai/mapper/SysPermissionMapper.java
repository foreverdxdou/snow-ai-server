package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.vo.SysPermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统权限Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询权限树
     *
     * @param parentId 父权限ID
     * @param type     权限类型
     * @param status   状态
     * @param name
     * @return 权限树
     */
    List<SysPermissionVO> selectPermissionTree(@Param("parentId") Long parentId, @Param("type") Integer type,
                                               @Param("status") Integer status, String name);

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> selectUserPermissions(@Param("userId") Long userId);

    /**
     * 根据角色ID列表查询权限列表
     *
     * @param roleIds 角色ID列表
     * @return 权限列表
     */
    List<SysPermission> selectPermissionsByRoleIds(@Param("roleIds") List<Long> roleIds);

}
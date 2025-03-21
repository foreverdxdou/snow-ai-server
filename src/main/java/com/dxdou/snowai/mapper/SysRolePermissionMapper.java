package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统角色权限Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 批量插入角色权限关联
     *
     * @param rolePermissions 角色权限关联列表
     */
    void insertBatch(List<SysRolePermission> rolePermissions);

}
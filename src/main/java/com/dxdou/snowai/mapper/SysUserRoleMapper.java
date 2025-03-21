package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户角色Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色关联列表
     */
    void insertBatch(List<SysUserRole> userRoles);

}
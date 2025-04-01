package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.domain.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param username 用户名
     * @param status   状态
     * @param deptId   部门ID
     * @param email
     * @param phone
     * @return 用户列表
     */
    Page<SysUserVO> selectUserList(Page<SysUser> page, @Param("username") String username,
                                   @Param("status") Integer status, @Param("deptId") Long deptId, @Param("email") String email, @Param("phone") String phone, @Param("nickName") String nickName);

}
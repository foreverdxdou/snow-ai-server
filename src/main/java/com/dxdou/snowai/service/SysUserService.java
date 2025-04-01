package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.SysPermission;
import com.dxdou.snowai.domain.entity.SysRole;
import com.dxdou.snowai.domain.entity.SysUser;
import com.dxdou.snowai.domain.vo.SysUserVO;

import java.util.List;

/**
 * 系统用户服务接口
 *
 * @author foreverdxdou
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getByUsername(String username);

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
    Page<SysUserVO> getUserPage(Page<SysUser> page, String username, Integer status, Long deptId, String email, String phone);

    /**
     * 创建用户
     *
     * @param user    用户信息
     * @param roleIds 角色ID列表
     */
    void createUser(SysUser user, Long[] roleIds);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void updateUser(SysUser user, Long[] roleIds);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     */
    void resetPassword(Long userId, String password);

    /**
     * 更新密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像URL
     */
    void updateUserAvatar(Long userId, String avatar);

    /**
     * 更新用户信息
     *
     * @param userId   用户ID
     * @param nickname 昵称
     * @param email    邮箱
     */
    void updateUserInfo(Long userId, String nickname, String email);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getRolesByUserId(Long userId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> getUserPermissions(Long userId);
}
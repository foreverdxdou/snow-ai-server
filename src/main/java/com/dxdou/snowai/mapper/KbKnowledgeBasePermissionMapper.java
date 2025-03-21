package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.KbKnowledgeBasePermission;
import com.dxdou.snowai.domain.vo.KbKnowledgeBasePermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库权限Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbKnowledgeBasePermissionMapper extends BaseMapper<KbKnowledgeBasePermission> {

    /**
     * 查询知识库的用户权限列表
     *
     * @param kbId 知识库ID
     * @return 权限列表
     */
    List<KbKnowledgeBasePermissionVO> selectUserPermissions(@Param("kbId") Long kbId);

    /**
     * 查询知识库的角色权限列表
     *
     * @param kbId 知识库ID
     * @return 权限列表
     */
    List<KbKnowledgeBasePermissionVO> selectRolePermissions(@Param("kbId") Long kbId);

    /**
     * 查询用户对知识库的权限类型
     *
     * @param userId 用户ID
     * @param kbId   知识库ID
     * @return 权限类型
     */
    Integer selectUserPermissionType(@Param("userId") Long userId, @Param("kbId") Long kbId);

    /**
     * 查询角色对知识库的权限类型
     *
     * @param roleId 角色ID
     * @param kbId   知识库ID
     * @return 权限类型
     */
    Integer selectRolePermissionType(@Param("roleId") Long roleId, @Param("kbId") Long kbId);

    /**
     * 删除知识库的所有权限
     *
     * @param kbId 知识库ID
     */
    void deleteByKbId(@Param("kbId") Long kbId);

    /**
     * 删除用户的权限
     *
     * @param kbId   知识库ID
     * @param userId 用户ID
     */
    void deleteByUserId(@Param("kbId") Long kbId, @Param("userId") Long userId);

    /**
     * 删除角色的权限
     *
     * @param kbId   知识库ID
     * @param roleId 角色ID
     */
    void deleteByRoleId(@Param("kbId") Long kbId, @Param("roleId") Long roleId);

    /**
     * 批量插入知识库权限
     *
     * @param permissions 权限列表
     */
    void insertBatch(@Param("permissions") List<KbKnowledgeBasePermission> permissions);
}
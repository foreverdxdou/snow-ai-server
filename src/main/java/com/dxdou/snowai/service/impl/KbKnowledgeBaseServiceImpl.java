package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbKnowledgeBase;
import com.dxdou.snowai.domain.entity.KbKnowledgeBasePermission;
import com.dxdou.snowai.domain.vo.KbKnowledgeBaseVO;
import com.dxdou.snowai.mapper.KbKnowledgeBaseMapper;
import com.dxdou.snowai.mapper.KbKnowledgeBasePermissionMapper;
import com.dxdou.snowai.service.KbKnowledgeBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbKnowledgeBaseServiceImpl extends ServiceImpl<KbKnowledgeBaseMapper, KbKnowledgeBase>
        implements KbKnowledgeBaseService {

    private final KbKnowledgeBaseMapper knowledgeBaseMapper;
    private final KbKnowledgeBasePermissionMapper permissionMapper;

    @Override
    public Page<KbKnowledgeBaseVO> getKnowledgeBasePage(Page<KbKnowledgeBase> page, String name, Long creatorId,
                                                        Integer status, Long categoryId) {
        return knowledgeBaseMapper.selectKnowledgeBaseList(page, name, creatorId, status, categoryId);
    }

    @Override
    public KbKnowledgeBaseVO getKnowledgeBaseById(Long id) {
        return knowledgeBaseMapper.selectKnowledgeBaseById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbKnowledgeBaseVO createKnowledgeBase(String name, String description, Long creatorId, Long categoryId) {
        // 1. 检查知识库名称是否重复
        if (isKnowledgeBaseNameExists(name)) {
            throw new BusinessException("知识库名称已存在");
        }

        // 2. 创建知识库
        KbKnowledgeBase knowledgeBase = new KbKnowledgeBase();
        knowledgeBase.setName(name);
        knowledgeBase.setDescription(description);
        knowledgeBase.setCategoryId(categoryId);
        knowledgeBase.setCreatorId(creatorId);
        knowledgeBase.setStatus(1);
        knowledgeBase.setCreateTime(LocalDateTime.now());
        knowledgeBase.setUpdateTime(LocalDateTime.now());
        knowledgeBaseMapper.insert(knowledgeBase);

        // 3. 为创建者分配管理权限
        KbKnowledgeBasePermission permission = new KbKnowledgeBasePermission();
        permission.setKbId(knowledgeBase.getId());
        permission.setUserId(creatorId);
        permission.setPermissionType(3);
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insert(permission);

        return getKnowledgeBaseById(knowledgeBase.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbKnowledgeBaseVO updateKnowledgeBase(Long id, String name, String description, Long categoryId) {
        // 1. 检查知识库是否存在
        KbKnowledgeBase knowledgeBase = getById(id);
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在");
        }

        // 2. 检查知识库名称是否重复
        if (!knowledgeBase.getName().equals(name) && isKnowledgeBaseNameExists(name)) {
            throw new BusinessException("知识库名称已存在");
        }

        // 3. 更新知识库
        knowledgeBase.setName(name);
        knowledgeBase.setDescription(description);
        knowledgeBase.setCategoryId(categoryId);
        knowledgeBase.setUpdateTime(LocalDateTime.now());
        updateById(knowledgeBase);

        return getKnowledgeBaseById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteKnowledgeBase(Long id) {
        // 1. 检查知识库是否存在
        KbKnowledgeBase knowledgeBase = getById(id);
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在");
        }

        // 2. 删除知识库
        removeById(id);

        // 3. 删除知识库权限
        permissionMapper.delete(new LambdaQueryWrapper<KbKnowledgeBasePermission>()
                .eq(KbKnowledgeBasePermission::getKbId, id));
    }

    @Override
    public void updateKnowledgeBaseStatus(Long id, Integer status) {
        // 1. 检查知识库是否存在
        KbKnowledgeBase knowledgeBase = getById(id);
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在");
        }

        // 2. 更新知识库状态
        knowledgeBase.setStatus(status);
        knowledgeBase.setUpdateTime(LocalDateTime.now());
        updateById(knowledgeBase);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignKnowledgeBasePermissions(Long kbId, List<Long> userIds, List<Long> roleIds,
            Integer permissionType) {
        // 1. 删除原有权限
        permissionMapper.delete(new LambdaQueryWrapper<KbKnowledgeBasePermission>()
                .eq(KbKnowledgeBasePermission::getKbId, kbId));

        // 2. 保存用户权限
        if (!CollectionUtils.isEmpty(userIds)) {
            List<KbKnowledgeBasePermission> userPermissions = userIds.stream()
                    .map(userId -> {
                        KbKnowledgeBasePermission permission = new KbKnowledgeBasePermission();
                        permission.setKbId(kbId);
                        permission.setUserId(userId);
                        permission.setPermissionType(permissionType);
                        permission.setCreateTime(LocalDateTime.now());
                        permission.setUpdateTime(LocalDateTime.now());
                        return permission;
                    })
                    .collect(Collectors.toList());
            permissionMapper.insertBatch(userPermissions);
        }

        // 3. 保存角色权限
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<KbKnowledgeBasePermission> rolePermissions = roleIds.stream()
                    .map(roleId -> {
                        KbKnowledgeBasePermission permission = new KbKnowledgeBasePermission();
                        permission.setKbId(kbId);
                        permission.setRoleId(roleId);
                        permission.setPermissionType(permissionType);
                        permission.setCreateTime(LocalDateTime.now());
                        permission.setUpdateTime(LocalDateTime.now());
                        return permission;
                    })
                    .collect(Collectors.toList());
            permissionMapper.insertBatch(rolePermissions);
        }
    }

    @Override
    public List<KbKnowledgeBaseVO> getUserKnowledgeBases(Long userId) {
        return knowledgeBaseMapper.selectUserKnowledgeBases(userId);
    }

    @Override
    public boolean hasPermission(Long userId, Long kbId, Integer permissionType) {
        return permissionMapper.selectCount(new LambdaQueryWrapper<KbKnowledgeBasePermission>()
                .eq(KbKnowledgeBasePermission::getKbId, kbId)
                .and(wrapper -> wrapper
                        .eq(KbKnowledgeBasePermission::getUserId, userId)
                        .or()
                        .inSql(KbKnowledgeBasePermission::getRoleId,
                                "SELECT role_id FROM sys_user_role WHERE user_id = " + userId))
                .ge(KbKnowledgeBasePermission::getPermissionType, permissionType)) > 0;
    }

    /**
     * 检查知识库名称是否已存在
     *
     * @param name 知识库名称
     * @return 是否已存在
     */
    private boolean isKnowledgeBaseNameExists(String name) {
        return knowledgeBaseMapper.selectCount(new LambdaQueryWrapper<KbKnowledgeBase>()
                .eq(KbKnowledgeBase::getName, name)
                .eq(KbKnowledgeBase::getDeleted, 0)) > 0;
    }
}
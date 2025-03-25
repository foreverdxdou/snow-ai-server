package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbCategory;
import com.dxdou.snowai.domain.vo.KbCategoryVO;
import com.dxdou.snowai.mapper.KbCategoryMapper;
import com.dxdou.snowai.service.KbCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识库分类服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbCategoryServiceImpl extends ServiceImpl<KbCategoryMapper, KbCategory> implements KbCategoryService {

    private final KbCategoryMapper categoryMapper;

    @Override
    public Page<KbCategoryVO> getCategoryPage(Page<KbCategory> page, String name, Integer status) {
        return categoryMapper.selectCategoryList(page, name, status);
    }

    @Override
    public KbCategoryVO getCategoryById(Long id) {
        return categoryMapper.selectCategoryById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbCategoryVO createCategory(Long kbId, String name, String description, Long parentId, Integer sort,
            Long creatorId) {
        // 1. 检查分类名称是否重复
        if (isCategoryNameExists(kbId, name)) {
            throw new BusinessException("分类名称已存在");
        }

        // 2. 检查父分类是否存在
        if (parentId != null) {
            KbCategory parentCategory = getById(parentId);
            if (parentCategory == null || !parentCategory.getKbId().equals(kbId)) {
                throw new BusinessException("父分类不存在");
            }
        }

        // 3. 创建分类
        KbCategory category = new KbCategory();
        category.setName(name);
        category.setDescription(description);
        category.setParentId(parentId);
        category.setKbId(kbId);
        category.setSort(sort != null ? sort : 0);
        category.setCreatorId(creatorId);
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(category);

        return getCategoryById(category.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbCategoryVO updateCategory(Long id, String name, String description, Long parentId, Integer sort) {
        // 1. 检查分类是否存在
        KbCategory category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 2. 检查分类名称是否重复
        if (!category.getName().equals(name) && isCategoryNameExists(category.getKbId(), name)) {
            throw new BusinessException("分类名称已存在");
        }

        // 3. 检查父分类是否存在
        if (parentId != null) {
            KbCategory parentCategory = getById(parentId);
            if (parentCategory == null || !parentCategory.getKbId().equals(category.getKbId())) {
                throw new BusinessException("父分类不存在");
            }
        }

        // 4. 更新分类
        category.setName(name);
        category.setDescription(description);
        category.setParentId(parentId);
        category.setSort(sort != null ? sort : category.getSort());
        category.setUpdateTime(LocalDateTime.now());
        updateById(category);

        return getCategoryById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 1. 检查分类是否存在
        KbCategory category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 2. 检查是否有子分类
        if (hasChildren(id)) {
            throw new BusinessException("存在子分类，无法删除");
        }

        // 3. 删除分类
        removeById(id);
    }

    @Override
    public void updateCategoryStatus(Long id, Integer status) {
        // 1. 检查分类是否存在
        KbCategory category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 2. 更新分类状态
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        updateById(category);
    }

    @Override
    public List<KbCategoryVO> getCategoryTree() {
        // 1. 获取分类树
        List<KbCategoryVO> categories = categoryMapper.selectCategoryTree();
        if (CollectionUtils.isEmpty(categories)) {
            return new ArrayList<>();
        }

        // 2. 构建树形结构
        Map<Long, List<KbCategoryVO>> parentIdMap = categories.stream()
                .collect(Collectors.groupingBy(KbCategoryVO::getParentId));

        categories.forEach(category -> {
            category.setChildren(parentIdMap.get(category.getId()));
        });

        // 3. 返回根节点列表
        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveCategory(Long id, Long parentId, Integer sort) {
        // 1. 检查分类是否存在
        KbCategory category = getById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }

        // 2. 检查父分类是否存在
        if (parentId != null) {
            KbCategory parentCategory = getById(parentId);
            if (parentCategory == null || !parentCategory.getKbId().equals(category.getKbId())) {
                throw new BusinessException("父分类不存在");
            }
        }

        // 3. 移动分类
        category.setParentId(parentId);
        category.setSort(sort != null ? sort : category.getSort());
        category.setUpdateTime(LocalDateTime.now());
        updateById(category);
    }

    /**
     * 检查分类名称是否已存在
     *
     * @param kbId 知识库ID
     * @param name 分类名称
     * @return 是否已存在
     */
    private boolean isCategoryNameExists(Long kbId, String name) {
        return categoryMapper.selectCount(new LambdaQueryWrapper<KbCategory>()
                .eq(KbCategory::getKbId, kbId)
                .eq(KbCategory::getName, name)
                .eq(KbCategory::getDeleted, 0)) > 0;
    }

    /**
     * 检查是否有子分类
     *
     * @param id 分类ID
     * @return 是否有子分类
     */
    private boolean hasChildren(Long id) {
        return categoryMapper.selectCount(new LambdaQueryWrapper<KbCategory>()
                .eq(KbCategory::getParentId, id)
                .eq(KbCategory::getDeleted, 0)) > 0;
    }
}
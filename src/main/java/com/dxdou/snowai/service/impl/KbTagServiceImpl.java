package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbTag;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.mapper.KbTagMapper;
import com.dxdou.snowai.service.KbTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 标签服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbTagServiceImpl extends ServiceImpl<KbTagMapper, KbTag> implements KbTagService {

    private final KbTagMapper tagMapper;

    @Override
    public Page<KbTagVO> getTagPage(Page<KbTag> page, String name, Long creatorId, Integer status) {
        return tagMapper.selectTagList(page, name, creatorId, status);
    }

    @Override
    public KbTagVO getTagById(Long id) {
        return tagMapper.selectTagById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbTagVO createTag(String name, String description, Long creatorId) {
        // 1. 检查标签名称是否重复
        if (isTagNameExists(name)) {
            throw new BusinessException("标签名称已存在");
        }

        // 2. 创建标签
        KbTag tag = new KbTag();
        tag.setName(name);
        tag.setDescription(description);
        tag.setStatus(1);
        tag.setCreateTime(LocalDateTime.now());
        tag.setCreatorId(creatorId);
        tag.setUpdateTime(LocalDateTime.now());
        save(tag);

        // 3. 返回标签信息
        return getTagById(tag.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbTagVO updateTag(Long id, String name, String description) {
        // 1. 检查标签是否存在
        KbTag tag = getById(id);
        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        // 2. 检查标签名称是否重复
        if (!tag.getName().equals(name) && isTagNameExists(name)) {
            throw new BusinessException("标签名称已存在");
        }

        // 3. 更新标签
        tag.setName(name);
        tag.setDescription(description);
        tag.setUpdateTime(LocalDateTime.now());
        updateById(tag);

        // 4. 返回标签信息
        return getTagById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        // 1. 检查标签是否存在
        KbTag tag = getById(id);
        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        // 2. 删除标签
        removeById(id);
    }

    @Override
    public void updateTagStatus(Long id, Integer status) {
        // 1. 检查标签是否存在
        KbTag tag = getById(id);
        if (tag == null) {
            throw new BusinessException("标签不存在");
        }

        // 2. 更新标签状态
        tag.setStatus(status);
        tag.setUpdateTime(LocalDateTime.now());
        updateById(tag);
    }

    /**
     * 检查标签名称是否已存在
     *
     * @param name 标签名称
     * @return 是否已存在
     */
    private boolean isTagNameExists(String name) {
        return tagMapper.selectCount(new LambdaQueryWrapper<KbTag>()
                .eq(KbTag::getName, name)
                .eq(KbTag::getDeleted, 0)) > 0;
    }
}
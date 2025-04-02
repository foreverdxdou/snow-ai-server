package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.dto.EmbeddingConfigDTO;
import com.dxdou.snowai.domain.entity.EmbeddingConfig;
import com.dxdou.snowai.domain.vo.EmbeddingConfigVO;
import com.dxdou.snowai.mapper.EmbeddingConfigMapper;
import com.dxdou.snowai.service.EmbeddingConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Embedding模型配置Service实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class EmbeddingConfigServiceImpl implements EmbeddingConfigService {

    private final EmbeddingConfigMapper embeddingConfigMapper;

    @Override
    public Page<EmbeddingConfigVO> getEmbeddingConfigPage(Page<EmbeddingConfig> page, String name, Integer status) {
        LambdaQueryWrapper<EmbeddingConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(EmbeddingConfig::getName, name);
        }
        if (status != null) {
            wrapper.eq(EmbeddingConfig::getStatus, status);
        }
        wrapper.orderByDesc(EmbeddingConfig::getStatus).orderByDesc(EmbeddingConfig::getCreateTime);

        Page<EmbeddingConfig> configPage = embeddingConfigMapper.selectPage(page, wrapper);
        Page<EmbeddingConfigVO> voPage = new Page<>(configPage.getCurrent(), configPage.getSize(),
                configPage.getTotal());

        voPage.setRecords(configPage.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public EmbeddingConfigVO getEmbeddingConfigById(Long id) {
        EmbeddingConfig config = embeddingConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("Embedding模型配置不存在");
        }
        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmbeddingConfigVO createEmbeddingConfig(EmbeddingConfigDTO dto, Long creatorId) {
        // 检查模型类型是否已存在
        LambdaQueryWrapper<EmbeddingConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmbeddingConfig::getModelType, dto.getModelType());
        if (embeddingConfigMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该模型类型已存在");
        }

        // 如果启用，则禁用其他配置
        if (dto.getStatus().equals(1)) {
            disableAllConfigs();
        }

        EmbeddingConfig config = new EmbeddingConfig();
        BeanUtils.copyProperties(dto, config);
        config.setCreatorId(creatorId);
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());

        embeddingConfigMapper.insert(config);
        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmbeddingConfigVO updateEmbeddingConfig(Long id, EmbeddingConfigDTO dto, Long updaterId) {
        EmbeddingConfig config = embeddingConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("Embedding模型配置不存在");
        }

        // 检查模型类型是否已存在（排除自身）
        LambdaQueryWrapper<EmbeddingConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmbeddingConfig::getModelType, dto.getModelType())
                .ne(EmbeddingConfig::getId, id);
        if (embeddingConfigMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该模型类型已存在");
        }

        // 如果启用，则禁用其他配置
        if (dto.getStatus().equals(1)) {
            disableAllConfigs();
        }

        BeanUtils.copyProperties(dto, config);
        config.setUpdateTime(LocalDateTime.now());
        config.setUpdaterId(updaterId);
        embeddingConfigMapper.updateById(config);
        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmbeddingConfig(Long id) {
        EmbeddingConfig config = embeddingConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("Embedding模型配置不存在");
        }
        embeddingConfigMapper.deleteById(id);
    }

    @Override
    public EmbeddingConfigVO getEnabledEmbeddingConfig() {
        LambdaQueryWrapper<EmbeddingConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmbeddingConfig::getStatus, 1);
        EmbeddingConfig config = embeddingConfigMapper.selectOne(wrapper);
        if (config == null) {
            throw new BusinessException("未找到启用的Embedding模型配置");
        }
        return convertToVO(config);
    }

    @Override
    public EmbeddingConfigVO updateEnabledStatus(Long id, Integer status) {
        if (status.equals(1)) {
            disableAllConfigs();
        }
        EmbeddingConfig embeddingConfig = new EmbeddingConfig();
        embeddingConfig.setStatus(status);
        embeddingConfig.setId(id);
        return embeddingConfigMapper.updateById(embeddingConfig) > 0 ? getEmbeddingConfigById(id) : null;
    }

    /**
     * 禁用所有配置
     */
    private void disableAllConfigs() {
        LambdaQueryWrapper<EmbeddingConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmbeddingConfig::getStatus, 1);
        EmbeddingConfig config = new EmbeddingConfig();
        config.setStatus(0);
        embeddingConfigMapper.update(config, wrapper);
    }

    /**
     * 转换为VO对象
     */
    private EmbeddingConfigVO convertToVO(EmbeddingConfig config) {
        EmbeddingConfigVO vo = new EmbeddingConfigVO();
        BeanUtils.copyProperties(config, vo);
        if (config.getStatus().equals(1)) {
            vo.setStatus(1);
        } else {
            vo.setStatus(0);
        }
        return vo;
    }
}
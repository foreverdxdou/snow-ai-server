package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.dto.SystemConfigDTO;
import com.dxdou.snowai.domain.entity.SystemConfig;
import com.dxdou.snowai.domain.vo.SystemConfigVO;
import com.dxdou.snowai.mapper.SystemConfigMapper;
import com.dxdou.snowai.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 系统配置Service实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigMapper systemConfigMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CONFIG_CACHE_KEY_PREFIX = "system:config:";
    private static final long CONFIG_CACHE_EXPIRE_TIME = 24 * 60 * 60; // 24小时

    @Override
    public Page<SystemConfigVO> getSystemConfigPage(Page<SystemConfig> page, String configKey, String configType) {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(configKey)) {
            wrapper.like(SystemConfig::getConfigKey, configKey);
        }
        if (StringUtils.hasText(configType)) {
            wrapper.eq(SystemConfig::getConfigType, configType);
        }
        wrapper.orderByDesc(SystemConfig::getCreateTime);

        Page<SystemConfig> configPage = systemConfigMapper.selectPage(page, wrapper);
        Page<SystemConfigVO> voPage = new Page<>(configPage.getCurrent(), configPage.getSize(), configPage.getTotal());

        voPage.setRecords(configPage.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    public SystemConfigVO getSystemConfigById(Long id) {
        SystemConfig config = systemConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("系统配置不存在");
        }
        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemConfigVO createSystemConfig(SystemConfigDTO dto, Long creatorId) {
        // 检查配置键是否已存在
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, dto.getConfigKey());
        if (systemConfigMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该配置键已存在");
        }

        SystemConfig config = new SystemConfig();
        BeanUtils.copyProperties(dto, config);
        config.setCreatorId(creatorId);
        config.setCreateTime(LocalDateTime.now());
        config.setUpdateTime(LocalDateTime.now());

        systemConfigMapper.insert(config);

        // 更新缓存
        updateCache(config);

        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemConfigVO updateSystemConfig(Long id, SystemConfigDTO dto, Long updaterId) {
        SystemConfig config = systemConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("系统配置不存在");
        }

        // 检查配置键是否已存在（排除自身）
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, dto.getConfigKey())
                .ne(SystemConfig::getId, id);
        if (systemConfigMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("该配置键已存在");
        }

        BeanUtils.copyProperties(dto, config);
        config.setUpdateTime(LocalDateTime.now());
        config.setUpdaterId(updaterId);
        systemConfigMapper.updateById(config);

        // 更新缓存
        updateCache(config);

        return convertToVO(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSystemConfig(Long id) {
        SystemConfig config = systemConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("系统配置不存在");
        }
        systemConfigMapper.deleteById(id);

        // 删除缓存
        deleteCache(config.getConfigKey());
    }

    @Override
    public String getConfigValue(String configKey) {
        return getConfigValue(configKey, null);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        // 先从缓存获取
        String cacheKey = CONFIG_CACHE_KEY_PREFIX + configKey;
        String value = redisTemplate.opsForValue().get(cacheKey);
        if (value != null) {
            return value;
        }

        // 缓存未命中，从数据库获取
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, configKey);
        SystemConfig config = systemConfigMapper.selectOne(wrapper);

        if (config == null) {
            return defaultValue;
        }

        // 更新缓存
        updateCache(config);

        return config.getConfigValue();
    }

    /**
     * 更新缓存
     */
    private void updateCache(SystemConfig config) {
        String cacheKey = CONFIG_CACHE_KEY_PREFIX + config.getConfigKey();
        redisTemplate.opsForValue().set(cacheKey, config.getConfigValue(), CONFIG_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     */
    private void deleteCache(String configKey) {
        String cacheKey = CONFIG_CACHE_KEY_PREFIX + configKey;
        redisTemplate.delete(cacheKey);
    }

    /**
     * 转换为VO对象
     */
    private SystemConfigVO convertToVO(SystemConfig config) {
        SystemConfigVO vo = new SystemConfigVO();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }
}
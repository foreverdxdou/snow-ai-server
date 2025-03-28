package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.dto.SystemConfigDTO;
import com.dxdou.snowai.domain.entity.SystemConfig;
import com.dxdou.snowai.domain.vo.SystemConfigVO;

/**
 * 系统配置Service接口
 *
 * @author foreverdxdou
 */
public interface SystemConfigService {

    /**
     * 分页查询系统配置列表
     *
     * @param page       分页参数
     * @param configKey  配置键
     * @param configType 配置类型
     * @return 分页结果
     */
    Page<SystemConfigVO> getSystemConfigPage(Page<SystemConfig> page, String configKey, String configType);

    /**
     * 获取系统配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    SystemConfigVO getSystemConfigById(Long id);

    /**
     * 创建系统配置
     *
     * @param dto       配置信息
     * @param creatorId 创建者ID
     * @return 配置详情
     */
    SystemConfigVO createSystemConfig(SystemConfigDTO dto, Long creatorId);

    /**
     * 更新系统配置
     *
     * @param id        配置ID
     * @param dto       配置信息
     * @param updaterId
     * @return 配置详情
     */
    SystemConfigVO updateSystemConfig(Long id, SystemConfigDTO dto, Long updaterId);

    /**
     * 删除系统配置
     *
     * @param id 配置ID
     */
    void deleteSystemConfig(Long id);

    /**
     * 获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);

    /**
     * 获取配置值（带默认值）
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfigValue(String configKey, String defaultValue);
}
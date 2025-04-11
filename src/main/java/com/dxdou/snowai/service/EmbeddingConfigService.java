package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.dto.EmbeddingConfigDTO;
import com.dxdou.snowai.domain.entity.EmbeddingConfig;
import com.dxdou.snowai.domain.vo.EmbeddingConfigVO;

/**
 * Embedding模型配置Service接口
 *
 * @author foreverdxdou
 */
public interface EmbeddingConfigService extends IService<EmbeddingConfig> {

    /**
     * 分页查询Embedding模型配置列表
     *
     * @param page    分页参数
     * @param name    模型名称
     * @param enabled 是否启用
     * @return 分页结果
     */
    Page<EmbeddingConfigVO> getEmbeddingConfigPage(Page<EmbeddingConfig> page, String name, Integer enabled);

    /**
     * 获取Embedding模型配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    EmbeddingConfigVO getEmbeddingConfigById(Long id);

    /**
     * 创建Embedding模型配置
     *
     * @param dto       配置信息
     * @param creatorId 创建者ID
     * @return 配置详情
     */
    EmbeddingConfigVO createEmbeddingConfig(EmbeddingConfigDTO dto, Long creatorId);

    /**
     * 更新Embedding模型配置
     *
     * @param id        配置ID
     * @param dto       配置信息
     * @param updaterId
     * @return 配置详情
     */
    EmbeddingConfigVO updateEmbeddingConfig(Long id, EmbeddingConfigDTO dto, Long updaterId);

    /**
     * 删除Embedding模型配置
     *
     * @param id 配置ID
     */
    void deleteEmbeddingConfig(Long id);

    /**
     * 获取启用的Embedding模型配置
     *
     * @return 配置详情
     */
    EmbeddingConfigVO getEnabledEmbeddingConfig();

    /**
     * 修改Embedding模型配置状态
     *
     * @param id
     * @param status
     * @return
     */
    EmbeddingConfigVO updateEnabledStatus(Long id, Integer status);
}
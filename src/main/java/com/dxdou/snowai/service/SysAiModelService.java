package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.SysAiModel;

import java.util.List;

/**
 * 系统AI模型服务接口
 *
 * @author foreverdxdou
 */
public interface SysAiModelService extends IService<SysAiModel> {

    /**
     * 分页查询模型列表
     *
     * @param page      分页参数
     * @param modelName 模型名称
     * @param modelType 模型类型
     * @param status    状态
     * @return 模型列表
     */
    Page<SysAiModel> getModelPage(Page<SysAiModel> page, String modelName, Integer modelType, Integer status);

    /**
     * 根据模型名称查询模型
     *
     * @param modelName 模型名称
     * @return 模型信息
     */
    SysAiModel getByModelName(String modelName);

    /**
     * 查询可用的模型列表
     *
     * @return 模型列表
     */
    List<SysAiModel> getAvailableModels();

    /**
     * 创建模型
     *
     * @param model 模型信息
     */
    void createModel(SysAiModel model);

    /**
     * 更新模型
     *
     * @param model 模型信息
     */
    void updateModel(SysAiModel model);

    /**
     * 删除模型
     *
     * @param modelId 模型ID
     */
    void deleteModel(Long modelId);

}
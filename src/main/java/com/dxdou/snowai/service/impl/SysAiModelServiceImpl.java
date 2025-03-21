package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.SysAiModel;
import com.dxdou.snowai.mapper.SysAiModelMapper;
import com.dxdou.snowai.service.SysAiModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统AI模型服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class SysAiModelServiceImpl extends ServiceImpl<SysAiModelMapper, SysAiModel> implements SysAiModelService {

    private final SysAiModelMapper aiModelMapper;

    @Override
    public Page<SysAiModel> getModelPage(Page<SysAiModel> page, String modelName, Integer modelType, Integer status) {
        return aiModelMapper.selectModelList(page, modelName, modelType, status);
    }

    @Override
    public SysAiModel getByModelName(String modelName) {
        return aiModelMapper.selectByModelName(modelName);
    }

    @Override
    public List<SysAiModel> getAvailableModels() {
        return aiModelMapper.selectAvailableModels();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createModel(SysAiModel model) {
        // 检查模型名称是否已存在
        if (aiModelMapper.selectCount(new LambdaQueryWrapper<SysAiModel>()
                .eq(SysAiModel::getModelName, model.getModelName())) > 0) {
            throw new BusinessException("模型名称已存在");
        }

        // 保存模型
        aiModelMapper.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(SysAiModel model) {
        // 检查模型名称是否已存在
        if (aiModelMapper.selectCount(new LambdaQueryWrapper<SysAiModel>()
                .eq(SysAiModel::getModelName, model.getModelName())
                .ne(SysAiModel::getId, model.getId())) > 0) {
            throw new BusinessException("模型名称已存在");
        }

        // 更新模型
        aiModelMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteModel(Long modelId) {
        aiModelMapper.deleteById(modelId);
    }
}
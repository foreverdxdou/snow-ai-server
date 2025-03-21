package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.SysAiModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统AI模型Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface SysAiModelMapper extends BaseMapper<SysAiModel> {

    /**
     * 分页查询模型列表
     *
     * @param page      分页参数
     * @param modelName 模型名称
     * @param modelType 模型类型
     * @param status    状态
     * @return 模型列表
     */
    Page<SysAiModel> selectModelList(Page<SysAiModel> page, @Param("modelName") String modelName,
            @Param("modelType") Integer modelType, @Param("status") Integer status);

    /**
     * 根据模型名称查询模型
     *
     * @param modelName 模型名称
     * @return 模型信息
     */
    SysAiModel selectByModelName(@Param("modelName") String modelName);

    /**
     * 查询可用的模型列表
     *
     * @return 模型列表
     */
    List<SysAiModel> selectAvailableModels();

}
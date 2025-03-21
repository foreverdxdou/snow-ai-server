package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.LlmConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 大模型配置Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface LlmConfigMapper extends BaseMapper<LlmConfig> {
}
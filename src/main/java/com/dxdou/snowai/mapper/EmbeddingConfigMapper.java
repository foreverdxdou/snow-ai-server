package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.EmbeddingConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * Embedding模型配置Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface EmbeddingConfigMapper extends BaseMapper<EmbeddingConfig> {
}
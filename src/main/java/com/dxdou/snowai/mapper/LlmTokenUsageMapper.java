package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.LlmTokenUsage;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI模型Token使用情况Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface LlmTokenUsageMapper extends BaseMapper<LlmTokenUsage> {
}
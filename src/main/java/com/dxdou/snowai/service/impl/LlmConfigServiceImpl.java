package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.domain.entity.LlmConfig;
import com.dxdou.snowai.mapper.LlmConfigMapper;
import com.dxdou.snowai.service.LlmConfigService;
import org.springframework.stereotype.Service;

/**
 * 大模型配置服务实现类
 *
 * @author foreverdxdou
 */
@Service
public class LlmConfigServiceImpl extends ServiceImpl<LlmConfigMapper, LlmConfig> implements LlmConfigService {
}
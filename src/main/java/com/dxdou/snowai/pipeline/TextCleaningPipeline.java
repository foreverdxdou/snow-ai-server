package com.dxdou.snowai.pipeline;

import com.dxdou.snowai.utils.TextCleanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanhengxue
 * @Date: 2025/04/15/13:56
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextCleaningPipeline {

    private final RedisTemplate<String, String> redisTemplate;

    public String process(String rawContent) {
        // TODO: 2025/4/15  1. 去噪处理
        String cleaned = TextCleanUtil.cleanHtml(rawContent);

        // TODO: 2025/4/15  2. 格式标准化

        // TODO: 2025/4/15  3. 脱敏处理

        // TODO: 2025/4/15  4. 去重检查

        // TODO: 2025/4/15  5. 质量验证
        return rawContent;
    }
}

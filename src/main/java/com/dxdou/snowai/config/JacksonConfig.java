package com.dxdou.snowai.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson全局配置
 *
 * @author foreverdxdou
 */
@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule javaLongModule() {
        SimpleModule module = new SimpleModule();
        // Long类型序列化为String
        module.addSerializer(Long.class, new ToStringSerializer());
        // long基本类型序列化为String
        module.addSerializer(Long.TYPE, new ToStringSerializer());
        return module;
    }
}


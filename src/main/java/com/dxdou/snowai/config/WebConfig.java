package com.dxdou.snowai.config;

import com.dxdou.snowai.security.SseAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Web配置类
 *
 * @author foreverdxdou
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SseAuthenticationInterceptor sseAuthenticationInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().responseTimeout(Duration.ZERO) // 禁用响应超时
                ))
                .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sseAuthenticationInterceptor)
                .addPathPatterns("/api/v1/kb/qa/chat/stream","/api/v1/kb/qa/general/stream");
    }
}
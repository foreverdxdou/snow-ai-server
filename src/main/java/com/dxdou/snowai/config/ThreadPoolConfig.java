package com.dxdou.snowai.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author foreverdxdou
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public void execute(Runnable task) {
                // 获取当前线程的MDC上下文
                Map<String, String> contextMap = MDC.getCopyOfContextMap();
                // 使用包装的Runnable来传递MDC上下文
                super.execute(() -> {
                    try {
                        // 设置MDC上下文
                        if (contextMap != null) {
                            MDC.setContextMap(contextMap);
                        }
                        // 执行任务
                        task.run();
                    } finally {
                        // 清理MDC上下文
                        MDC.clear();
                    }
                });
            }
        };

        // 配置线程池
        executor.setCorePoolSize(10); // 核心线程数
        executor.setMaxPoolSize(20); // 最大线程数
        executor.setQueueCapacity(200); // 队列容量
        executor.setKeepAliveSeconds(60); // 线程空闲时间
        executor.setThreadNamePrefix("async-executor-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();
        return executor;
    }
}
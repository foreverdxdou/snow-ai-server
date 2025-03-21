package com.dxdou.snowai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI知识库系统启动类
 *
 * @author foreverdxdou
 */
@SpringBootApplication
@MapperScan("com.dxdou.snowai.mapper")
public class SnowAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowAiApplication.class, args);
    }

}
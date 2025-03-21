package com.dxdou.snowai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j API文档配置类
 *
 * @author foreverdxdou
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Snow AI Knowledge Base API")
                        .version("1.0.0")
                        .description("AI知识库系统接口文档")
                        .contact(new Contact()
                                .name("foreverdxdou")
                                .email("foreverdxdou@gmail.com")));
    }
}
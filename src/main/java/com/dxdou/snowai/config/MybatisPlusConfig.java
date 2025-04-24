package com.dxdou.snowai.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.apache.ibatis.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyBatis-Plus配置类
 *
 * @author foreverdxdou
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    /**
     * 自定义SQL日志实现类
     */
    public static class CustomStdOutImpl implements Log {
        private static String preparingTemp;
        private Logger log;

        public CustomStdOutImpl(String clazz) {
            // 不需要实现
            this.log = LoggerFactory.getLogger(clazz);
        }

        @Override
        public boolean isDebugEnabled() {
            return this.log.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return this.log.isTraceEnabled();
        }

        @Override
        public void error(String s, Throwable e) {
            this.log.error(s, e);
        }

        @Override
        public void error(String s) {
            this.log.error(s);
        }

        @Override
        public void debug(String s) {
            this.log.debug(s);
            if (s != null && s.contains("Preparing")) {
                preparingTemp = s;
                return;
            }
            if (s != null && s.contains("Parameters")) {
                String preparing = preparingTemp != null ? preparingTemp.substring("==>  Preparing: ".length()) : "";
                preparingTemp = null;
                String parameters = s.substring("==> Parameters: ".length());
                if (preparing.contains("?")) {
                    formatLog(preparing, parameters);
                    return;
                }
            }
        }

        private void formatLog(String preparing, String parameters) {
            String[] params = parameters.split(", ");
            for (String param : params) {
                if (param != null && param.length() != 0 && preparing != null && preparing.length() != 0
                        && param.indexOf("(") != -1) {
                    preparing = preparing.replaceFirst(" \\?", " '" + param.substring(0, param.indexOf("(")) + "'");
                } else if (param != null && param.length() != 0 && preparing != null && preparing.length() != 0) {
                    preparing = preparing.replaceFirst(" \\?", " null");
                }
            }
            log.debug("==> SQL: " + preparing);
        }

        @Override
        public void trace(String s) {
            System.out.println(s);
        }

        @Override
        public void warn(String s) {
            System.out.println(s);
        }
    }
}
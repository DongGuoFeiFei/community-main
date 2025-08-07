package com.example.communityserver.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * <p>
 * 配置全局时区
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-05
 **/


@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 上海时区 = UTC+8
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        };
    }
}

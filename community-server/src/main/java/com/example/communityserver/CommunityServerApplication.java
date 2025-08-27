package com.example.communityserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.example.communityserver.mapper","com.example.communityserver.chat.mapper"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@EnableRetry // 启用重试功能
@EnableAsync // 如果还需要异步支持
public class CommunityServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityServerApplication.class, args);
    }

}

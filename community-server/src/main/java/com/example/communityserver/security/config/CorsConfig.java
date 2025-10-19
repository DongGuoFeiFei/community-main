package com.example.communityserver.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns(
                        "http://www.caifurong.top",
                        "http://caifurong.top",
                        "http://api.caifurong.top",
                        "http://www.api.caifurong.top",
                        "http://admin.caifurong.top",
                        "http://www.admin.caifurong.top",
                        "https://www.caifurong.top",
                        "https://caifurong.top",
                        "https://api.caifurong.top",
                        "https://www.api.caifurong.top",
                        "https://admin.caifurong.top",
                        "https://www.admin.caifurong.top",
                        "http://localhost:5173",
                        "http://localhost:5174"
                )
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}

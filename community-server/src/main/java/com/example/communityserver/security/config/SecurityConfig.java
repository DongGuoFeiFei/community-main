package com.example.communityserver.security.config;

import com.example.communityserver.filter.JWTFilter;
import com.example.communityserver.handler.AccessDeniedHandlerImpl;
import com.example.communityserver.handler.AuthenticationEntryPointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTFilter jwtFilter;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;

    public SecurityConfig(JWTFilter jwtFilter,
                          AccessDeniedHandlerImpl accessDeniedHandler,
                          AuthenticationEntryPointImpl authenticationEntryPoint) {
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    // TODO: 2025/8/9 添加权限配置,添加接口的统一认证
//    @Bean
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/admin/**").access("@perms.hasPerm('system:admin')")
//                .antMatchers("/user/add").access("@perms.hasPerm('system:user:add')")
//                .anyRequest().authenticated();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF
                .csrf(csrf -> csrf.disable())
                // 不通过Session获取SecurityContext
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 授权配置
                .authorizeHttpRequests(auth -> auth
                        // 允许匿名访问的接口
                        .antMatchers(
                                "/auth/login",
                                "/auth/admin/login",
                                "/auth/register",
                                "/auth/send-email",
                                "/auth/captcha",
                                "/auth/registerCode",
                                "/uploads/**",
                                "/swagger-ui.html",
                                "/doc.html",
                                "/webjars/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/v2/**"
                        ).permitAll()
                        // 显式声明重要接口请求需要认证
                        .antMatchers(
                                "/posts/**"
                        ).authenticated()
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )
                // 异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                // 添加JWT过滤器
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                // 允许跨域
                .cors(cors -> cors.configure(http));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

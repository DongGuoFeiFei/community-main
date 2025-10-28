package com.example.communityserver.core.config;

import com.example.communityserver.core.security.filter.JWTFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .csrf(
                        csrf -> csrf
                                .disable()
//                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                // 不通过Session获取SecurityContext
                .sessionManagement(
                        session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 授权配置
                .authorizeHttpRequests(
                        auth -> auth
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
                                "/v2/**",
                                "/api/websocket/*",
                                "/ws/**",
                                "/ws",
                                "/websocket/**",
                                "/sockjs/**",
                                "/ws/**",
                                "/websocket/**",
                                "/sockjs/**",
                                "/app/**",
                                "/topic/**",
                                "/user/**"
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

    // 配置CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
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
        ));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

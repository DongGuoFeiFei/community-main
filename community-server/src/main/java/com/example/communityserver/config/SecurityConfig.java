package com.example.communityserver.config;


import com.example.communityserver.filter.JWTFilter;
import com.example.communityserver.handler.AccessDeniedHandlerImpl;
import com.example.communityserver.handler.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity的配置类要求继承WebSecurityConfigurerAdapter
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTFilter jwtFilter;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers(
                        "/auth/login",
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
                )
                .anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        http// 将自己定义的过滤器加到UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置异常处理器
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //允许跨域，保证跨域配置不被阻止
        http.cors();
    }


    /*
    将AuthenticationManager放入容器中
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

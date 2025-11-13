package com.example.communityserver.core.security.filter;

import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.core.security.util.JWTUtil;
import com.example.communityserver.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/registerCode",
            "/auth/send-email",
            "/auth/captcha",
            "/uploads/**",
            "/auth/admin/login",
            "/swagger-ui.html",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/v2/**",
            "/api/websocket/*",
            "/auth/reset-password/**",
            "/auth/forgot-password"
    );

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = req.getRequestURI();

        // 白名单直接放行
        if (isWhiteList(requestURI)) {
            filterChain.doFilter(req, resp);
            return;
        }

        // 放行WebSocket握手请求
        String path = req.getRequestURI();
        if (path.startsWith("/ws") || path.startsWith("/websocket")) {
            filterChain.doFilter(req, resp);
            return;
        }

        String token = req.getHeader("token");

        try {
            // 1. 检查Token是否存在
            if (token == null || token.isEmpty()) {
                setUnauthenticatedContext("请提供访问令牌");
                filterChain.doFilter(req, resp);
                return;
            }

            // 2. 验证Token有效性
            if (!JWTUtil.verifyToken(token)) {
                setUnauthenticatedContext("访问令牌无效或已过期");
                filterChain.doFilter(req, resp);
                return;
            }

            // 3. 获取用户信息
            Long userId = JWTUtil.getUserId(token);
            LoginUser loginUser = redisUtil.getCacheObject(CacheKeyConstants.LOGIN_USER_ID + userId);

            if (loginUser == null) {
                setUnauthenticatedContext("用户登录信息已过期，请重新登录");
                filterChain.doFilter(req, resp);
                return;
            }

            // 4. 认证成功，设置Authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(req, resp);

        } catch (Exception e) {
            // 处理其他异常
            SecurityContextHolder.clearContext();
            throw new ServletException(e);
        }
    }

    /**
     * 设置未认证的安全上下文
     *
     * @param message 错误信息
     */
    private void setUnauthenticatedContext(String message) {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken(
                        "anonymous",
                        "anonymous",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
                ) {
                    @Override
                    public Object getDetails() {
                        return message;
                    }
                }
        );
    }

    // 检查请求URI是否在白名单中
    private boolean isWhiteList(String requestURI) {
        for (String whitePath : WHITE_LIST) {
            // 情况1：完全匹配（如 /auth/login）
            if (whitePath.equals(requestURI)) {
                return true;
            }
            // 情况2：Ant风格通配符（如 /uploads/**）
            if (antPathMatcher.match(whitePath, requestURI)) {
                return true;
            }
            // 情况3：正则表达式（如 /posts/\\d+）
            try {
                if (Pattern.matches(whitePath, requestURI)) {
                    return true;
                }
            } catch (PatternSyntaxException e) {
                // 忽略非正则表达式的路径（如 /uploads/**）
            }
        }
        return false;
    }
}

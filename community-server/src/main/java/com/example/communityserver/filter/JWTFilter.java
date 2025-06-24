package com.example.communityserver.filter;

import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.utils.security.JWTUtil;
import com.example.communityserver.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    // 定义白名单路径
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/send-email",
            "/uploads/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = req.getRequestURI();

        // 1. 检查是否是白名单路径
        if (isWhiteList(requestURI)) {
            filterChain.doFilter(req, resp);
            return;
        }

        // 2. 获取token
        String token = req.getHeader("token");

        // 3. 没有token的情况处理
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(req, resp);
            return;
        }

        // 4. 验证token有效性
        if (!JWTUtil.verifyToken(token)) {
            throw new RuntimeException("token失效，请重新登录");
        }

        // 5. 从token中获取userId
        Long userId = JWTUtil.getUserId(token);

        // 6. 从redis获取用户信息
        LoginUser loginUser = redisUtil.getCacheObject("user:" + userId);
        if (loginUser == null) {
            throw new RuntimeException("用户信息已过期，请重新登录");
        }

        // 7. 创建Authentication对象并设置到安全上下文
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 8. 放行
        filterChain.doFilter(req, resp);
    }

    // 检查请求URI是否在白名单中
    private boolean isWhiteList(String requestURI) {
        return WHITE_LIST.stream().anyMatch(whitePath ->
                requestURI.equals(whitePath) ||
                        (whitePath.contains("**") && requestURI.startsWith(whitePath.replace("**", "")))
        );
    }
}

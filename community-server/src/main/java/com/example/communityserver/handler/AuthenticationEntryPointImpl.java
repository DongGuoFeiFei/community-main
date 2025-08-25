package com.example.communityserver.handler;

import com.alibaba.fastjson.JSON;
import com.example.communityserver.utils.web.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 认证入口点实现类，用于处理认证失败的情况
 * @Param:
 * @return:
 * @Author: DongGuo
 */

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    /**
     * 认证失败时的处理方法
     * 当用户认证失败时，此方法会被调用
     *
     * @param request       HttpServletRequest对象，包含请求信息
     * @param response      HttpServletResponse对象，用于返回响应
     * @param authException 认证异常对象，包含认证失败的详细信息
     * @throws IOException      可能抛出IO异常
     * @throws ServletException 可能抛出Servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf8");
        response.getWriter().write(JSON.toJSONString(Result.error(401, authException.getMessage() != null ? authException.getMessage() : "认证失败")));
    }
}

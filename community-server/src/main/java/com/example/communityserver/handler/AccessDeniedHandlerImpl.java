package com.example.communityserver.handler;

import com.alibaba.fastjson.JSON;
import com.example.communityserver.utils.web.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 访问被拒绝处理器实现类，用于处理用户权限不足时的访问拒绝情况
 * @Param:
 * @return:
 * @Author: DongGuo
 */

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    /**
     * 处理访问被拒绝异常的方法
     * 当用户没有权限访问请求的资源时，此方法会被调用
     *
     * @param request               HttpServletRequest对象，包含客户端请求信息
     * @param response              HttpServletResponse对象，用于向客户端返回响应
     * @param accessDeniedException AccessDeniedException对象，包含访问被拒绝的异常信息
     * @throws IOException      可能发生I/O异常
     * @throws ServletException 可能发生Servlet异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf8");
        response.getWriter().write(JSON.toJSONString(Result.error(403, accessDeniedException.getMessage() != null ? accessDeniedException.getMessage() : "授权失败")));
    }
}

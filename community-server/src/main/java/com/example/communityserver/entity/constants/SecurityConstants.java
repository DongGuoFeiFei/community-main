package com.example.communityserver.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 安全相关常量
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


public class SecurityConstants {

    // JWT相关
    public static final String TOKEN_HEADER = "Authorization"; // 请求头中的token字段
    public static final String TOKEN_PREFIX = "Bearer "; // token前缀
    public static final long TOKEN_EXPIRATION = 3600 * 24 * 7; // token过期时间，7天
    public static final String JWT_SECRET = "your-secret-key"; // JWT密钥，实际项目中应从配置文件中读取

    // 权限相关
    public static final String ROLE_ADMIN = "ROLE_ADMIN"; // 管理员角色
    public static final String ROLE_USER = "ROLE_USER"; // 用户角色
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR"; // 普通用户角色

    // 登录相关
    public static final String LOGIN_URL = "/auth/login"; // 登录接口
    public static final String LOGOUT_URL = "/auth/logout"; // 登出接口
    public static final String REGISTER_URL = "/auth/register"; // 注册接口
}
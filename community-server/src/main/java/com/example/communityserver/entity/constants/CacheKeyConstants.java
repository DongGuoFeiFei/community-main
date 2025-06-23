package com.example.communityserver.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 缓存键常量
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


public class CacheKeyConstants {

    // 用户相关缓存键
    public static final String USER_INFO = "user:info:"; // 后接用户ID
    public static final String USER_PERMISSIONS = "user:permissions:"; // 后接用户ID

    // 文章相关缓存键
    public static final String ARTICLE_VIEW_COUNT = "article:view:count:"; // 后接文章ID
    public static final String ARTICLE_LIKE_COUNT = "article:like:count:"; // 后接文章ID

    // 验证码相关
    public static final String CAPTCHA_CODE = "captcha:code:"; // 后接验证码ID

    // 系统统计
    public static final String SITE_STATISTICS = "site:statistics"; // 系统统计
}

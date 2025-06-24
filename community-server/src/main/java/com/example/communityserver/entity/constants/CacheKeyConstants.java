package com.example.communityserver.entity.constants;

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
    public static final String USER_INFO = "user:info:";
    public static final String USER_PERMISSIONS = "user:permissions:";

    // 文章相关缓存键
    public static final String ARTICLE_VIEW_COUNT = "article:view:count:";
    public static final String ARTICLE_LIKE_COUNT = "article:like:count:";
    public static final String ARTICLE_COMMENT = "article:comment:";

    // 验证码相关
    public static final String CAPTCHA_CODE = "captcha:code:";

    // 系统统计
    public static final String SITE_STATISTICS = "site:statistics";

    private static final String REDIS_VERIFY_CODE_PREFIX = "verify:email:";
}

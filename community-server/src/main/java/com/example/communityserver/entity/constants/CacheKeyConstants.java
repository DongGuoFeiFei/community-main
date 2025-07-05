package com.example.communityserver.entity.constants;

/**
 * <p>
 * 缓存键常量
 * <p>
 *
 * @author_ DongGuo
 * @create_ 2025-06-23
 **/


public class CacheKeyConstants {

    // 用户相关缓存键
    public static final String LOGIN_USER_INFO = "login:user:info:";
    public static final String LOGIN_USER_ID = "login_user_id:";
    public static final String USER_FOLLOWING_COUNT = "user:following:count:";
    public static final String USER_FOLLOWER_COUNT = "user:follow:count:";
    public static final String USER_ARTICLE_COUNT = "user:article:count:";

    public static final String LOGIN_USER_PERMISSIONS = "login:user:permissions:";

    // 文章相关缓存键
    public static final String ARTICLE_VIEW_COUNT = "article:view:count:";
    public static final String ARTICLE_LIKE_COUNT = "article:like:count:";

    public static final String ARTICLE_COMMENT = "article:comment:";

    // 验证码相关
    public static final String CAPTCHA_CODE = "captcha:code:";

    // 系统统计
    public static final String SITE_STATISTICS = "site:statistics:";

    private static final String REDIS_VERIFY_CODE_PREFIX = "verify:email:";
}

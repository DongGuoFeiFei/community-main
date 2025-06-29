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
    public static final String LOGIN_USER_INFO = "login_user_info:";
    public static final String LOGIN_USER_ID = "login_user_id:";

    public static final String LOGIN_USER_PERMISSIONS = "login_user_permissions:";

    // 文章相关缓存键
    public static final String ARTICLE_VIEW_COUNT = "article_view_count:";
    public static final String ARTICLE_LIKE_COUNT = "article_like_count:";
    public static final String ARTICLE_COMMENT = "article_comment:";

    // 验证码相关
    public static final String CAPTCHA_CODE = "captcha_code:";

    // 系统统计
    public static final String SITE_STATISTICS = "site_statistics:";

    private static final String REDIS_VERIFY_CODE_PREFIX = "verify_email:";
}

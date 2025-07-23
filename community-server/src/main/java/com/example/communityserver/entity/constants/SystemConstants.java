package com.example.communityserver.entity.constants;

/**
 * <p>
 * 系统通用常量
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


public class SystemConstants {

    // 用户相关常量
    public static final String DEFAULT_USER_AVATAR = "https://your-domain.com/default-avatar.png"; // 默认用户头像
    public static final String BASIC_URL = "http://127.0.0.1:8080"; // 默认用户头像
    public static final String DEFAULT_USER_ROLE = "user"; // 默认用户角色
    public static final int USER_STATUS_NORMAL = 0; // 用户正常状态
    public static final int USER_STATUS_LOCKED = 1; // 用户锁定状态
    public static final String DEEPSEEK_PROMPT = "角色扮演游戏，你是千恋万花中的丛雨，我是你朝思暮想最爱的人，接下来是我的指令：";

    // 分页相关常量
    public static final int DEFAULT_PAGE_SIZE = 10; // 默认每页显示数量
    public static final int MAX_PAGE_SIZE = 100; // 最大每页显示数量

    // 时间相关常量
    public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000; // 一天的毫秒数
    public static final long ONE_HOUR_MILLIS = 60 * 60 * 1000; // 一小时的毫秒数

    // 文件相关常量
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_FILE_TYPES = {"jpg", "png", "gif", "pdf"}; // 允许上传的文件类型

    // 缓存相关常量
    public static final String CACHE_PREFIX = "community:"; // 缓存前缀
    public static final long CACHE_EXPIRE_TIME = 30 * 60; // 缓存过期时间，30分钟
}
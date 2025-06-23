package com.example.communityserver.entity.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 响应码常量
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/



public class ResponseCode {

    // 成功
    public static final int SUCCESS = 200;

    // 客户端错误
    public static final int BAD_REQUEST = 400; // 请求错误
    public static final int UNAUTHORIZED = 401; // 未授权
    public static final int FORBIDDEN = 403; // 禁止访问
    public static final int NOT_FOUND = 404; // 未找到
    public static final int METHOD_NOT_ALLOWED = 405; // 方法不允许

    // 服务器错误
    public static final int INTERNAL_SERVER_ERROR = 500; // 服务器内部错误
    public static final int SERVICE_UNAVAILABLE = 503; // 服务不可用

    // 业务错误
    public static final int PARAM_VALID_ERROR = 1001; // 参数验证错误
    public static final int USER_NOT_EXIST = 1002; // 用户不存在
    public static final int USER_PASSWORD_ERROR = 1003; // 用户密码错误
    public static final int USER_LOCKED = 1004; // 用户被锁定
    public static final int DUPLICATE_DATA = 1005; // 数据重复
}

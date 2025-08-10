package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 返回相应数据
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    // 其他
    USERNAME_EXIST(200, "用户名已经存在"),
    EMAIL_EXIST(200, "邮箱已经存在"),
    PHONE_EXIST(200, "手机号已经存在"),
    REGISTER_SUCCESS(200, "注册成功"),
    REGISTER_ERROR(500, "注册失败"),

    // 成功
    SUCCESS(200, "成功"),

    // 客户端错误
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    // 服务器错误
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务错误
    PARAM_VALID_ERROR(1001, "参数验证错误"),
    USER_NOT_EXIST(1002, "用户不存在"),
    USER_PASSWORD_ERROR(1003, "用户密码错误"),
    USER_LOCKED(1004, "用户被锁定"),
    DUPLICATE_DATA(1005, "数据重复");

    private final Integer code;
    private final String value;
}

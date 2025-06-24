package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Getter
@AllArgsConstructor
public enum MessageCodeEnum {
    USERNAME_EXIST(1, 200, "用户名已经存在"),
    EMAIL_EXIST(2, 200, "邮箱已经存在"),
    REGISTER_SUCCESS(3, 200, "注册成功"),
    REGISTER_ERROR(4, 500, "注册失败");

    private final Integer id;
    private final Integer code;
    private final String value;
}

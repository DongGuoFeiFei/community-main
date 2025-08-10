package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码类型枚举类
 * <p>
 * 该枚举类用于定义系统中不同类型的验证码
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Getter
@AllArgsConstructor
public enum ValidateCodeTypeEnum {
    REGISTER("register");

    private final String value;
}

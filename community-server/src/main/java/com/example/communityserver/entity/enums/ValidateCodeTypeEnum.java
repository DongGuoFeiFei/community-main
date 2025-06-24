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
public enum ValidateCodeTypeEnum {
    REGISTER("register");

    private final String value;
}

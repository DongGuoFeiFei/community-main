package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-24
 **/

@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("ROLE_ADMIN", "管理员"),
    USER("ROLE_USER", "普通用户"),
    GUEST("ROLE_GUEST", "访客");

    private final String authority;
    private final String description;
}

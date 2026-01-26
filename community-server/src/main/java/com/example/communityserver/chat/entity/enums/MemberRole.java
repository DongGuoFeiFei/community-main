package com.example.communityserver.chat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 成员角色枚举
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Getter
@AllArgsConstructor
public enum MemberRole {

    /**
     * 普通成员
     */
    MEMBER(1, "普通成员"),

    /**
     * 管理员
     */
    ADMIN(2, "管理员"),

    /**
     * 群主
     */
    OWNER(3, "群主");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static MemberRole getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MemberRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }
}

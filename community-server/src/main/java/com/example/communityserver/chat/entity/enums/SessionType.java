package com.example.communityserver.chat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话类型枚举
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Getter
@AllArgsConstructor
public enum SessionType {
    
    /** 私聊 */
    PRIVATE(1, "私聊"),
    
    /** 群聊 */
    GROUP(2, "群聊");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static SessionType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SessionType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}

package com.example.communityserver.chat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态枚举
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Getter
@AllArgsConstructor
public enum MessageStatus {
    
    /** 正常 */
    NORMAL(1, "正常"),
    
    /** 已撤回 */
    RECALLED(2, "已撤回"),
    
    /** 已删除 */
    DELETED(3, "已删除");

    private final Integer code;
    private final String desc;

    /**
     * 根据code获取枚举
     */
    public static MessageStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MessageStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}

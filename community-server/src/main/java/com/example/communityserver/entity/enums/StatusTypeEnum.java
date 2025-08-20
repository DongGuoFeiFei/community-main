package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/

@Getter
@AllArgsConstructor
public enum StatusTypeEnum {
    // 处理方式；待处理 已处理 已忽略
    PENDING(0, "待处理"),
    PROCESSED(1, "已处理"),
    IGNORED(2, "已忽略"),
    // 同意，拒绝
    AGREE(3, "同意"),
    REFUSE(4, "拒绝"),
    // 禁言，解禁
    BAN(5, "禁言"),
    UNBAN(6, "解禁"),
    // 禁止访问，解除禁止访问
    BAN_ACCESS(7, "禁止访问"),
    UNBAN_ACCESS(8, "解除禁止访问");
    private static final Map<Integer, StatusTypeEnum> KEY_MAP = new HashMap<>();

    static {
        for (StatusTypeEnum type : values()) {
            KEY_MAP.put(type.getKey(), type);
        }
    }

    private final Integer key;
    private final String label;

    public static StatusTypeEnum fromKey(Integer key) {
        StatusTypeEnum type = KEY_MAP.get(key);
        if (type == null) {
            throw new IllegalArgumentException("No enum constant with key: " + key);
        }
        return type;
    }
}

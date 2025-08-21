package com.example.communityserver.entity.enums;

import lombok.Getter;

/**
 * 积分操作类型枚举
 */
@Getter
public enum PointsOperationType {
    EARN(1, "获取积分"),
    CONSUME(2, "消费积分"),
    FREEZE(3, "冻结积分"),
    UNFREEZE(4, "解冻积分"),
    ADJUST(5, "调整积分");

    private final int code;
    private final String desc;

    PointsOperationType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PointsOperationType getByCode(int code) {
        for (PointsOperationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }
}

package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 通知枚举
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-25
 **/

@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    LIKE("like"),
    COMMENT("comment"),
    FOLLOW("follow"),
    SYSTEM("system"),
    REPLY("reply");

    private final String value;
}

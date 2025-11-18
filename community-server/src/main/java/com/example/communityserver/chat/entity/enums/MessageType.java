package com.example.communityserver.chat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 消息类型枚举
 * 1文本 2图片 3文件 4音频 5视频 6系统
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-20
 **/
@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT(1),
    IMAGE(2),
    FILE(3),
    AUDIO(4),
    VIDEO(5),
    SYSTEM(6);

    private final int value;
}

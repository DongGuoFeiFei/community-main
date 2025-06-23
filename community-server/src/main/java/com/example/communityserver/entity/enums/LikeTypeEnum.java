package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 点赞的枚举类型
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-21
 **/


@Getter
@AllArgsConstructor
public enum LikeTypeEnum {
    COMMENT("comment"),
    ARTICLE("article"),
    FOLLOW("follow");
    private final String value;
}

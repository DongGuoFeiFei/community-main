package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-18
 **/

@Getter
@AllArgsConstructor
public enum LikeTypeEnum {
    COMMENT("comment", "评论", "#2ED573"),    // 清新绿色
    ARTICLE("article", "回复", "#1E90FF");    // 明亮蓝色

    private final String value;
    private final String label;
    private final String color;
}

package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 点赞类型枚举类
 * <p>
 *  该枚举定义了系统中不同的点赞类型，包括评论点赞和文章点赞
 * 每种类型都关联了一个值、标签和对应的颜色代码
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

package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章交互类型枚举类
 * <p>
 *  该枚举定义了用户与文章交互的不同类型，包括浏览、点赞和分享等行为。
 * 每种交互类型都对应一个唯一的编码和描述值。
 * <p>
 * @author: DongGuo
 * @author: DongGuo
 * @create: 2025-07-07
 **/

@Getter
@AllArgsConstructor
public enum ArticleInteractionTypeEnum {
    VIEW(1, "浏览"),
    LIKE(2, "点赞"),
    SHARE(4, "分享");

    private final Integer code;
    private final String value;
}

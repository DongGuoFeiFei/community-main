package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-07
 **/

@Getter
@AllArgsConstructor
public enum ArticleInteractionTypeEnum {
    VIEW(1, "浏览"),
    LIKE(2, "点赞"),
    COMMENT(3, "评论"),
    SHARE(4, "分享");

    private final Integer code;
    private final String value;
}

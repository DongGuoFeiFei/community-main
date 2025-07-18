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
public enum ActiveTypeEnum {
    LIKE("like", "点赞", "#FF4757"),         // 鲜艳红色
    ARTICLE_LIKE("articleLike", "文章点赞", "#FF4757"),         // 鲜艳红色
    COMMENT_LIKE("commentLike", "评论点赞", "#FF4757"),         // 鲜艳红色
    COMMENT("comment", "评论", "#2ED573"),    // 清新绿色
    COMMENT_REPLY("commentReply", "回复", "#1E90FF"),    // 明亮蓝色
    FOLLOW("follow", "关注", "#9C51B6"),      // 深紫色
    SYSTEM("system", "系统", "#FF7F50"),       // 珊瑚橙
    FAVORITE("favorite", "收藏", "#25CCF7"),  // 天蓝色
    REPLY("reply", "回复", "#FFD700"),        // 金色
    FAVORITE_ARTICLE("favoriteArticle", "收藏文章", "#58B19F"),
    PRIVATE_MESSAGES("privateMessages", "私信", "#59B89F"); // 蓝绿色

    private final String value;
    private final String label;
    private final String color;
}

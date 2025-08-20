package com.example.communityserver.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户的行为枚举
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-25
 **/

@Getter
@AllArgsConstructor
public enum ActiveTypeEnum {

    // 通知类型
    LIKE(1, "like", "点赞", "#FF4757"),         // 鲜艳红色
    ARTICLE_LIKE(2, "articleLike", "文章点赞", "#FF4757"),         // 鲜艳红色
    COMMENT_LIKE(3, "commentLike", "评论点赞", "#FF4757"),         // 鲜艳红色
    COMMENT(4, "comment", "评论", "#2ED573"),    // 清新绿色
    COMMENT_REPLY(5, "commentReply", "回复", "#1E90FF"),    // 明亮蓝色
    FOLLOW(6, "follow", "关注", "#9C51B6"),      // 深紫色
    SYSTEM(7, "system", "系统", "#FF7F50"),       // 珊瑚橙
    FAVORITE(8, "favorite", "收藏", "#25CCF7"),  // 天蓝色
    REPLY(9, "reply", "回复", "#FFD700"),        // 金色
    FAVORITE_ARTICLE(10, "favoriteArticle", "收藏文章", "#58B19F"),
    PRIVATE_MESSAGES(11, "privateMessages", "私信", "#59B89F"), // 蓝绿色

    // 举报内容
    REPORT_ARTICLE(12, "report_article", "举报文章", "#FF4757"),
    REPORT_AUTHOR(13, "report_author", "举报作者", "#FF4757");

    private static final Map<Integer, ActiveTypeEnum> KEY_MAP = new HashMap<>();

    static {
        for (ActiveTypeEnum type : values()) {
            KEY_MAP.put(type.getKey(), type);
        }
    }

    private final Integer key;
    private final String value;
    private final String label;
    private final String color;

    public static ActiveTypeEnum fromKey(Integer key) {
        ActiveTypeEnum type = KEY_MAP.get(key);
        if (type == null) {
            throw new IllegalArgumentException("No enum constant with key: " + key);
        }
        return type;
    }
}

package com.example.communityserver.entity.response;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-07
 **/

@Data
public class UnreadCountByTypeVo {
    private Long like;
    private Long comment;
    private Long follow;
    private Long system;
    private Long favorite;
    private Long reply;
    private Long favoriteArticle;
    private Long privateMessages;
}

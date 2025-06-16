package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Data
public class AddFavoriteArticle {
    private Long articleId;
    private Long folderId;
}

package com.example.communityserver.entity.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

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
    @NotNull
    private Long articleId;
    @NotNull
    private Long folderId;
}

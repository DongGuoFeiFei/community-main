package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Data
public class MoveFavoriteDto {
    private Long articleId;
    private Long folderId;
    private Long activeFolderId;
}

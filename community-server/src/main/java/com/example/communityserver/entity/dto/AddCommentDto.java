package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-04
 **/

@Data
public class AddCommentDto {
    private Long articleId;
    private String content;
    private Long parentId;
}

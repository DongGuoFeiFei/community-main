package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-01
 **/

@Data
public class AddArticleDto {
    private String title;
    private Long fileId;
    private String content;
}

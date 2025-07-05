package com.example.communityserver.entity.request;

import lombok.Data;

import java.util.List;

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
    private Integer status;
    private List<Long> tagIds;
}

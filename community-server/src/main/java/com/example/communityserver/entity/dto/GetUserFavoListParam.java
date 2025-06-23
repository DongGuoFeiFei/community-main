package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-22
 **/

@Data
public class GetUserFavoListParam {
    private Integer page;
    private Integer size;
    // 搜索关键词
    private String keyword;
    private Long folderId;
}

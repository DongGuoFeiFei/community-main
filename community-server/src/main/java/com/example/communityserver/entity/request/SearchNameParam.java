package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 *  搜索，分页
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-24
 **/

@Data
public class SearchNameParam {
    private String title;
    private Integer pageSize;
    private Integer pageNum;
}

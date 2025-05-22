package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-21
 **/

@Data
public class GetArticleListDto {
    private Integer page;           // 当前页码
    private Integer size;          // 每页条数
    private String title;               // 文章标题搜索
    private Integer status;             // 状态：0-草稿，1-已发布
    private String sortField = "createTime"; // 排序字段：createTime/updateTime/viewCount
    private Boolean isAsc = false;      // 是否升序

}

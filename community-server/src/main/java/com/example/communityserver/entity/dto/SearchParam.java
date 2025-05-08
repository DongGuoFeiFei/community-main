package com.example.communityserver.entity.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;

/**
 * <p>
 *  搜索，分页
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-24
 **/

@Data
public class SearchParam {
    private String title;
    private Integer pageSize;
    private Integer pageNum;
}

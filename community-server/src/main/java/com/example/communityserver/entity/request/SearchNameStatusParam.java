package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Data
public class SearchNameStatusParam {
    private String name;
    private String keyword; // 同name，不同接口使用名称差异
    private Integer status;
    private Integer page;
    private Integer size;
}

package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-17
 **/
@Data
public class MenuSearchParam {

    private String menuName;
    private String menuType;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;

}

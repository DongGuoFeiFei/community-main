package com.example.communityserver.entity.request;

import io.swagger.models.auth.In;
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
public class SearchNameParam {
    private String name;
    private Integer status;
    private Integer page;
    private Integer size;
}

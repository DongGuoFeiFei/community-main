package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-05
 **/

@Data
public class CreateTagParam {
    private String name;
    private String slug;
    private String color;
}

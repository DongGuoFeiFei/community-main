package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-10
 **/

@Data
public class UserSearchParam {
    private String username;
    private String email;
    private String status;
    private String roleId;
    private Integer page;
    private Integer size;
}

package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-13
 **/

@Data
public class RoleSearchFormParam {
    private String roleName;
    private String roleKey;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}

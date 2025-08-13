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
public class AddRoleParam {
    private Long roleId;
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private Integer status;
    private String remark;
}

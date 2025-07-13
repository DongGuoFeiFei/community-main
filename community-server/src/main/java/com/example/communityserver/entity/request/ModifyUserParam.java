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
public class ModifyUserParam {
    private Long userId;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer isActive;
}

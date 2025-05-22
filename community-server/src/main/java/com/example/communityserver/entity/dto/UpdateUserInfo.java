package com.example.communityserver.entity.dto;

import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-10
 **/

@Data
public class UpdateUserInfo {
    private String username;
    private String nickname;
    private String email;
    private String phone;
}

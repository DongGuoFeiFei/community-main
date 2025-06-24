package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-24
 **/

@Data
public class RegisterDto {
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String code;
}

package com.example.communityserver.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserListVo {
    private Long userId;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Integer isActive;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lastLogin;
    private String avatar;
    private String fullName;

    private String bio;

    private Integer gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String birthDate;
}

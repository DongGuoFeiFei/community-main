package com.example.communityserver.entity.vo;

import com.example.communityserver.entity.po.User;
import lombok.Data;

/**
 * <p>
 * 登录响应
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-21
 **/


@Data
public class LoginResponse {
    private String token;
    private String tokenType;
    private Integer expiresIn; //token有效时间
    private User userInfo;
    private String avatarUrl;
    // 如果需要刷新令牌可以添加
//     TODO: 2025/4/21  刷新令牌添加
    // private String refreshToken;
}
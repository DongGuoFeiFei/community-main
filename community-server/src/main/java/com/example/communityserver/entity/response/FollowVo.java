package com.example.communityserver.entity.response;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-16
 **/

@Data
public class FollowVo {
    private Long id;
    private String avatar;
    private String bio;
    private String nickname;
    private String isFollowing;
}

package com.example.communityserver.entity.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-02
 **/

@Data
public class AuthorInfoVo {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String bio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String joinDate;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
    private Boolean isFollowing;
}

package com.example.communityserver.chat.entity.response;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-18
 **/

@Data
public class SessionDetailVo {
    private Long sessionId;
    private String avatar;
    private String name;
    private Long userId;
    private String nickname;
}

package com.example.communityserver.entity.response;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-06
 **/

@Data
public class UserCountStats {
    private Long postCount;
    private Long followers;
    private Long following;
}

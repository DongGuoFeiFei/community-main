package com.example.communityserver.entity.response;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Data
public class DashboardDataVo {
    private Long totalUsers;
    private Long todayNewUsers;
    private Long totalPosts;
    private Long todayNewPosts;
    private Long totalComments;
    private Long activeUsers;
}

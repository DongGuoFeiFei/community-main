package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.response.DashboardDataVo;
import com.example.communityserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Service
public class DashboardServiceImpl implements IDashboardService {
    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ILoginLogService loginLogService;

    @Override
    public DashboardDataVo getDashboardData() {
        DashboardDataVo vo = new DashboardDataVo();
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrowStart = todayStart.plusDays(1);

        Long totalUsers = userService.count();
        vo.setTotalUsers(totalUsers);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();

        userLambdaQueryWrapper.ge(User::getCreateTime, todayStart)
                .lt(User::getCreateTime, tomorrowStart);
        Long todayNewUsers = userService.count(userLambdaQueryWrapper);
        vo.setTodayNewUsers(todayNewUsers);

        Long totalPosts = articleService.count();
        vo.setTotalPosts(totalPosts);
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.ge(Article::getCreatedAt, todayStart)
                .lt(Article::getCreatedAt, tomorrowStart);

        Long todayNewPosts = articleService.count(articleLambdaQueryWrapper);
        vo.setTodayNewPosts(todayNewPosts);

        Long totalComments = commentService.count();
        vo.setTotalComments(totalComments);

        Long activeUsers;
        // TODO: 2025/7/8 用户活跃获取
        vo.setActiveUsers(totalUsers);

        return vo;
    }
}

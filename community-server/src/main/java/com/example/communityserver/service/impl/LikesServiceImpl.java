package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.*;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.mapper.ArticleInteractionMapper;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.ILikesService;
import com.example.communityserver.utils.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/

@Service
@Slf4j
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;

    /**
     * @Description: 获取article_like
     * @Param: [param]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.example.communityserver.entity.response.NotificationListVo>
     * @Author: DongGuo
     */

    @Override
    public IPage<NotificationListVo> getArticleLikeNotificationsVo(GetNotificationsParam param) {
        IPage<Notification> notificationPage = new Page<>(param.getPage(), param.getSize());

        log.info("{}", param);
        LambdaQueryWrapper<Notification> notificationQueryWrapper = new LambdaQueryWrapper<>();
        notificationQueryWrapper
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, param.getType())
                .eq(Notification::getIsDel, 0)
                .orderByAsc(Notification::getIsRead)
                .orderByDesc(Notification::getCreatedAt);
        IPage<Notification> entityPage = notificationMapper.selectPage(notificationPage, notificationQueryWrapper);
        List<Long> sendIds = entityPage.getRecords().stream().map(Notification::getSenderId).distinct().collect(Collectors.toList());
        List<Long> likeIds = entityPage.getRecords().stream().map(Notification::getContentId).distinct().collect(Collectors.toList());

        List<ArticleInteraction> articleInteractions = likeIds.isEmpty() ? Collections.emptyList() : articleInteractionMapper.selectBatchIds(likeIds);
        List<Long> articleIds = articleInteractions.stream().map(ArticleInteraction::getArticleId).distinct().collect(Collectors.toList());

        List<User> users = sendIds.isEmpty() ? Collections.emptyList() : userMapper.selectBatchIds(sendIds);
        List<Article> articles = articleIds.isEmpty() ? Collections.emptyList() : articleService.listByIds(articleIds);

        // 转换为VO列表
        List<NotificationListVo> listVos = entityPage
                .getRecords()
                .stream()
                .map(record -> {
                    NotificationListVo vo = new NotificationListVo();
                    vo.setNotificationId(record.getNotificationId());
                    vo.setType(record.getType());
                    vo.setIsRead(record.getIsRead() == 1);

                    // 设置发送者信息
                    users.stream()
                            .filter(user -> user.getUserId().equals(record.getSenderId()))
                            .findFirst()
                            .ifPresent(user -> {
                                vo.setSenderId(user.getUserId());
                                vo.setSenderName(user.getNickname());
                                vo.setSenderAvatar(user.getAvatar());
                            });
                    // 设置文章信息
                    articleInteractions
                            .stream()
                            .filter(articleInteraction -> articleInteraction.getId().equals(record.getContentId()))
                            .findFirst()
                            .ifPresent(articleInteraction -> {
                                articles.stream()
                                        .filter(article -> article.getArticleId().equals(articleInteraction.getArticleId()))
                                        .findFirst()
                                        .ifPresent(article -> {
                                            vo.setSourceId(article.getArticleId());
                                            vo.setSourceTitle(article.getTitle());
                                        });
                            });

                    vo.setCreatedAt(record.getCreatedAt());
                    vo.setExtraData(record.getExtraData());

                    return vo;
                })
                .collect(Collectors.toList());


        // 构建返回分页对象
        IPage<NotificationListVo> page = new Page<>();
        page.setRecords(listVos);
        page.setTotal(entityPage.getTotal());
        page.setCurrent(entityPage.getCurrent());
        page.setSize(entityPage.getSize());
        return page;
    }
}

package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ArticleInteractionTypeEnum;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.ArticleInteraction;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.mapper.ArticleInteractionMapper;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.service.IArticleInteractionService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-21
 **/

@Service
public class ArticleInteractionServiceImpl extends ServiceImpl<ArticleInteractionMapper, ArticleInteraction> implements IArticleInteractionService {
    @Autowired
    private ArticleInteractionMapper articleInteractionMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public boolean addArticleLike(Long articleId) {
        LambdaQueryWrapper<ArticleInteraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleInteraction::getArticleId, articleId)
                .eq(ArticleInteraction::getUserId, SecurityUtils.getLoginUserId())
                .eq(ArticleInteraction::getActionType, ArticleInteractionTypeEnum.LIKE);
        boolean exists = articleInteractionMapper.exists(queryWrapper);
        if (exists) {
            return false;
        }
        ArticleInteraction interaction = new ArticleInteraction(articleId, SecurityUtils.getLoginUserId(), ArticleInteractionTypeEnum.LIKE, null);
        int insert = articleInteractionMapper.insert(interaction);
        if (insert < 0) {
            return false;
        }
        // 通知
        Article article = articleMapper.selectById(articleId);
        Notification notification = new Notification(article.getUserId(), ActiveTypeEnum.ARTICLE_LIKE, interaction.getId(), SecurityUtils.getLoginUserId());
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, article.getArticleId())
                .setSql("like_count = like_count + 1");
        articleMapper.update(null, updateWrapper);
        notificationMapper.insert(notification);
        redisUtil.deleteObject(CacheKeyConstants.ARTICLE_LIKE_COUNT + articleId);
        return true;
    }

    @Override
    @Transactional
    public boolean delArticleLike(Long articleId) {
        LambdaQueryWrapper<ArticleInteraction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ArticleInteraction::getArticleId, articleId)
                .eq(ArticleInteraction::getUserId, SecurityUtils.getLoginUserId())
                .eq(ArticleInteraction::getActionType, ArticleInteractionTypeEnum.LIKE);
        ArticleInteraction interaction = articleInteractionMapper.selectOne(queryWrapper);
        if (interaction == null) {
            return false;
        }
        articleInteractionMapper.delete(queryWrapper);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, articleId)
                .setSql("like_count = like_count - 1");
        articleMapper.update(null, updateWrapper);
        LambdaQueryWrapper<Notification> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1
                .eq(Notification::getType, ActiveTypeEnum.ARTICLE_LIKE)
                .eq(Notification::getSenderId, SecurityUtils.getLoginUserId())
                .eq(Notification::getContentId, interaction.getArticleId());
        notificationMapper.delete(queryWrapper1);
        redisUtil.deleteObject(CacheKeyConstants.ARTICLE_LIKE_COUNT + articleId);
        return true;

    }
}

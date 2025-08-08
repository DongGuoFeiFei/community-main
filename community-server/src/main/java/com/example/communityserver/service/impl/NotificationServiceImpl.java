package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.*;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.MarkAsReadParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.UnreadCountByTypeVo;
import com.example.communityserver.mapper.*;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.service.ILikesService;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.utils.common.DateUtils;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/
@Slf4j
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ILikesService likesService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public IPage<NotificationListVo> getNotifications(GetNotificationsParam param) {
        assert param.getType() != null;
        if (param.getType().equals(ActiveTypeEnum.LIKE.getValue())) {
            param.setType("ARTICLE_LIKE");
            return likesService.getArticleLikeNotificationsVo(param);
        } else if (param.getType().equals(ActiveTypeEnum.COMMENT.getValue())) {
            // TODO: 2025/8/7 未考虑到评论被删除或者被拒绝的情况
            return commentService.getCommentNotificationsVo(param);
        } else if (param.getType().equals(ActiveTypeEnum.REPLY.getValue())) {
            // TODO: 2025/8/7 未考虑到评论被删除或者被拒绝的情况
            return this.getReplyNotificationsVo(param);
        } else if (param.getType().equals(ActiveTypeEnum.FAVORITE_ARTICLE.getValue())) {
            param.setType("FAVORITE_ARTICLE");
            return this.getFavoriteArticleNotificationsVo(param);
        } else if (param.getType().equals(ActiveTypeEnum.FOLLOW.getValue())) {
            return this.getFollowNotificationsVo(param);
        }
        log.info("无效通知类型");
        return null;
    }

    /**
     * @Description: 回复评论通知
     * @Param: [param]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.example.communityserver.entity.response.NotificationListVo>
     * @Author: DongGuo
     */

    private IPage<NotificationListVo> getReplyNotificationsVo(GetNotificationsParam param) {
        IPage<Notification> notificationPage = new Page<>(param.getPage(), param.getSize());

        log.info("{}", param);
        LambdaQueryWrapper<Notification> notificationQueryWrapper = new LambdaQueryWrapper<>();
        notificationQueryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.REPLY).eq(Notification::getIsDel, 0).orderByAsc(Notification::getIsRead).orderByDesc(Notification::getCreatedAt);
        IPage<Notification> notificationIPage = notificationMapper.selectPage(notificationPage, notificationQueryWrapper);

        List<Long> sendIds = notificationIPage.getRecords().stream().map(Notification::getSenderId).distinct().collect(Collectors.toList());
        List<User> senders = sendIds.isEmpty() ? Collections.emptyList() : userMapper.selectBatchIds(sendIds);

        List<Long> contentIds = notificationIPage.getRecords().stream().map(Notification::getContentId).distinct().collect(Collectors.toList());
        List<Comment> comments = contentIds.isEmpty() ? Collections.emptyList() : commentMapper.selectBatchIds(contentIds);
        List<Long> articleIds = comments.stream().map(Comment::getArticleId).distinct().collect(Collectors.toList());
        List<Article> articles = articleIds.isEmpty() ? Collections.emptyList() : articleMapper.selectBatchIds(articleIds);

        // 发起者用户senders  文章名称articles  评论内容comments  通知数据
        //转换为VO列表
        List<NotificationListVo> listVos = notificationIPage.getRecords().stream().map(notification -> {
            NotificationListVo vo = new NotificationListVo();
            vo.setNotificationId(notification.getNotificationId());
            vo.setType(notification.getType());
            vo.setIsRead(notification.getIsRead() == 1);
            // 设置发送者信息
            senders.stream().filter(sender -> sender.getUserId().equals(notification.getSenderId())).findFirst().ifPresent(sender -> {
                vo.setSenderId(sender.getUserId());
                vo.setSenderName(sender.getNickname());
                vo.setSenderAvatar(sender.getAvatar());
            });
            // 设置文章信息
            comments.stream().filter(comment -> comment.getCommentId().equals(notification.getContentId())).findFirst().ifPresent(comment -> {
                articles.stream().filter(article -> article.getArticleId().equals(comment.getArticleId())).findFirst().ifPresent(article -> {
                    vo.setSourceId(article.getArticleId());
                    vo.setSourceTitle(article.getTitle());
                    vo.setRelatedId(comment.getCommentId());
                    vo.setRelatedContent(comment.getContent());
                    vo.setCreatedAt(DateUtils.parseSafe(comment.getCreatedAt()));
                });
            });
//                    vo.setCreatedAt(notification.getCreatedAt());
            vo.setExtraData(notification.getExtraData());
            return vo;
        }).collect(Collectors.toList());
        // 构建返回分页对象
        IPage<NotificationListVo> page = new Page<>();
        page.setRecords(listVos);
        page.setTotal(notificationIPage.getTotal());
        page.setCurrent(notificationIPage.getCurrent());
        page.setSize(notificationIPage.getSize());
        return page;
    }

    /**
     * @Description: 关注
     * @Param: [param]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.example.communityserver.entity.response.NotificationListVo>
     * @Author: DongGuo
     */

    private IPage<NotificationListVo> getFollowNotificationsVo(GetNotificationsParam param) {
        IPage<Follow> followPage = new Page<>(param.getPage(), param.getSize());

        LambdaQueryWrapper<Follow> followQueryWrapper = new LambdaQueryWrapper<>();
        followQueryWrapper.eq(Follow::getFollowerId, SecurityUtils.getLoginUserId()).orderByDesc(Follow::getCreatedAt);
        IPage<Follow> followIPage = followMapper.selectPage(followPage, followQueryWrapper);

        List<Long> followIds = followIPage.getRecords().stream().map(Follow::getId).collect(Collectors.toList());
        List<Long> followingIds = followIPage.getRecords().stream().map(Follow::getFollowingId).collect(Collectors.toList());
        List<User> senders = userMapper.selectBatchIds(followingIds);

        LambdaQueryWrapper<Notification> notificationQueryWrapper = new LambdaQueryWrapper<>();
        notificationQueryWrapper.in(Notification::getContentId, followIds).eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.FOLLOW).eq(Notification::getIsDel, 0);

        List<Notification> notificationList = notificationMapper.selectList(notificationQueryWrapper);

        //转换为VO列表
        List<NotificationListVo> listVos = followIPage.getRecords().stream().map(follow -> {
            NotificationListVo vo = new NotificationListVo();
            vo.setCreatedAt(follow.getCreatedAt());
            vo.setType(ActiveTypeEnum.FOLLOW);
            // 设置发送者信息
            senders.stream().filter(sender -> sender.getUserId().equals(follow.getFollowingId())).findFirst().ifPresent(sender -> {
                vo.setSenderId(sender.getUserId());
                vo.setSenderName(sender.getNickname());
                vo.setSenderAvatar(sender.getAvatar());
            });
            // 设置文章信息
            notificationList.stream().filter(notification -> follow.getId().equals(notification.getContentId())).findFirst().ifPresent(notification -> {
                vo.setNotificationId(notification.getNotificationId());
                vo.setExtraData(notification.getExtraData());
                vo.setIsRead(notification.getIsRead() == 1);
                vo.setIsDeleted(notification.getIsDel() == 1);
            });
            return vo;
        }).collect(Collectors.toList());
        // 构建返回分页对象
        IPage<NotificationListVo> page = new Page<>();
        page.setRecords(listVos);
        page.setTotal(followIPage.getTotal());
        page.setCurrent(followIPage.getCurrent());
        page.setSize(followIPage.getSize());
        return page;
    }

    /**
     * @Description: 获取收藏文章通知
     * @Param: [param]
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.example.communityserver.entity.response.NotificationListVo>
     * @Author: DongGuo
     */

    private IPage<NotificationListVo> getFavoriteArticleNotificationsVo(GetNotificationsParam param) {
        IPage<Notification> notificationPage = new Page<>(param.getPage(), param.getSize());

        LambdaQueryWrapper<Notification> notificationQueryWrapper = new LambdaQueryWrapper<>();
        notificationQueryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.FAVORITE_ARTICLE).eq(Notification::getIsDel, 0).orderByAsc(Notification::getIsRead).orderByDesc(Notification::getCreatedAt);
        IPage<Notification> notificationIPage = notificationMapper.selectPage(notificationPage, notificationQueryWrapper);

        List<Long> sendIds = notificationIPage.getRecords().stream().map(Notification::getSenderId).distinct().collect(Collectors.toList());
        List<User> senders = sendIds.isEmpty() ? Collections.emptyList() : userMapper.selectBatchIds(sendIds);

        List<Long> contentIds = notificationIPage.getRecords().stream().map(Notification::getContentId).distinct().collect(Collectors.toList());
        List<UserFavorite> userFavorites = contentIds.isEmpty() ? Collections.emptyList() : userFavoriteMapper.selectBatchIds(contentIds);

        List<Long> articleIds = userFavorites.stream().filter(userFavorite -> userFavorite.getType().equals(ActiveTypeEnum.FAVORITE_ARTICLE)).map(UserFavorite::getTargetId).distinct().collect(Collectors.toList());
        List<Article> articles = articleIds.isEmpty() ? Collections.emptyList() : articleMapper.selectBatchIds(articleIds);

        // 发起者用户senders  文章名称articles  userFavorites  通知数据
        //转换为VO列表
        List<NotificationListVo> listVos = notificationIPage.getRecords().stream().map(notification -> {
            NotificationListVo vo = new NotificationListVo();
            vo.setNotificationId(notification.getNotificationId());
            vo.setType(notification.getType());
            vo.setIsRead(notification.getIsRead() == 1);
            // 设置发送者信息
            senders.stream().filter(sender -> sender.getUserId().equals(notification.getSenderId())).findFirst().ifPresent(sender -> {
                vo.setSenderId(sender.getUserId());
                vo.setSenderName(sender.getNickname());
                vo.setSenderAvatar(sender.getAvatar());
            });
            // 设置文章信息
            userFavorites.stream().filter(userFavorite -> userFavorite.getFavoriteId().equals(notification.getContentId())).findFirst().ifPresent(userFavorite -> {
                articles.stream().filter(article -> article.getArticleId().equals(userFavorite.getTargetId())).findFirst().ifPresent(article -> {
                    vo.setSourceId(article.getArticleId());
                    vo.setSourceTitle(article.getTitle());
                    vo.setRelatedId(userFavorite.getFavoriteId());
                });
            });
            vo.setCreatedAt(notification.getCreatedAt());
            vo.setExtraData(notification.getExtraData());
            return vo;
        }).collect(Collectors.toList());
        // 构建返回分页对象
        IPage<NotificationListVo> page = new Page<>();
        page.setRecords(listVos);
        page.setTotal(notificationIPage.getTotal());
        page.setCurrent(notificationIPage.getCurrent());
        page.setSize(notificationIPage.getSize());
        return page;
    }

    /**
     * @Description: 逻辑删除通知
     * @Param: [param]
     * @return: java.lang.Integer
     * @Author: DongGuo
     */


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer markAsRead(MarkAsReadParam param) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        if (param.getType().equals("like")) {
            param.setType("ARTICLE_LIKE");
        }
        updateWrapper.in(Notification::getNotificationId, param.getIds()).eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0).eq(Notification::getType, param.getType().toUpperCase()).set(Notification::getIsRead, 1);
        int update = notificationMapper.update(null, updateWrapper);

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo == null) {
            return update;
        }
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, param.getType().toUpperCase()).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        switch (param.getType()) {
            case "ARTICLE_LIKE" -> {
                vo.setLike(notificationMapper.selectCount(queryWrapper));
            }
            case "comment" -> {
                vo.setComment(notificationMapper.selectCount(queryWrapper));
            }
            case "follow" -> {
                vo.setFollow(notificationMapper.selectCount(queryWrapper));
            }
            case "system" -> {
                vo.setSystem(notificationMapper.selectCount(queryWrapper));
            }
            case "favorite" -> {
                vo.setFavorite(notificationMapper.selectCount(queryWrapper));
            }
            case "reply" -> {
                vo.setReply(notificationMapper.selectCount(queryWrapper));
            }
            case "favoriteArticle" -> {
                vo.setFavoriteArticle(notificationMapper.selectCount(queryWrapper));
            }
            case "privateMessages" -> {
                vo.setPrivateMessages(notificationMapper.selectCount(queryWrapper));
            }
            default -> throw new IllegalArgumentException("无效类型: " + param.getType());
        }

        redisUtil.setCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), vo, 3, TimeUnit.DAYS);

        return update;

    }

    @Override
    public Integer deleteNotifications(MarkAsReadParam param) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Notification::getNotificationId, param.getIds()).eq(Notification::getUserId, SecurityUtils.getLoginUserId()).setSql("is_del = 1 - is_del");

        int update = notificationMapper.update(null, updateWrapper);
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo == null) {
            return update;
        }

        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, param.getType().toUpperCase()).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        switch (param.getType()) {
            case "like" -> {
                vo.setLike(notificationMapper.selectCount(queryWrapper));
            }
            case "comment" -> {
                vo.setComment(notificationMapper.selectCount(queryWrapper));
            }
            case "follow" -> {
                vo.setFollow(notificationMapper.selectCount(queryWrapper));
            }
            case "system" -> {
                vo.setSystem(notificationMapper.selectCount(queryWrapper));
            }
            case "favorite" -> {
                vo.setFavorite(notificationMapper.selectCount(queryWrapper));
            }
            case "reply" -> {
                vo.setReply(notificationMapper.selectCount(queryWrapper));
            }
            case "favoriteArticle" -> {
                vo.setFavoriteArticle(notificationMapper.selectCount(queryWrapper));
            }
            case "privateMessages" -> {
                vo.setPrivateMessages(notificationMapper.selectCount(queryWrapper));
            }
            default -> throw new IllegalArgumentException("无效类型: " + param.getType());
        }
        redisUtil.setCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), vo, 3, TimeUnit.DAYS);

        return update;
    }

    /**
     * @Description: 通知物理删除
     * @Param: [type, contentId]
     * @return: java.lang.Integer
     * @Author: DongGuo
     */

    @Override
    public Integer deleteNotification(ActiveTypeEnum type, Long contentId, Long receiverId) {
        if (type == null) {
            return -1;
        }
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getType, type).eq(Notification::getSenderId, SecurityUtils.getLoginUserId()).eq(Notification::getContentId, contentId);
        int update = notificationMapper.delete(queryWrapper);
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + receiverId);

        if (vo == null) {
            return update;
        }
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, type).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);

        switch (type.getValue()) {
            case "like" -> {
                vo.setLike(notificationMapper.selectCount(queryWrapper));
            }
            case "comment" -> {
                vo.setComment(notificationMapper.selectCount(queryWrapper));
            }
            case "follow" -> {
                vo.setFollow(notificationMapper.selectCount(queryWrapper));
            }
            case "system" -> {
                vo.setSystem(notificationMapper.selectCount(queryWrapper));
            }
            case "favorite" -> {
                vo.setFavorite(notificationMapper.selectCount(queryWrapper));
            }
            case "reply" -> {
                vo.setReply(notificationMapper.selectCount(queryWrapper));
            }
            case "favoriteArticle" -> {
                vo.setFavoriteArticle(notificationMapper.selectCount(queryWrapper));
            }
            case "privateMessages" -> {
                vo.setPrivateMessages(notificationMapper.selectCount(queryWrapper));
            }
            default -> throw new IllegalArgumentException("无效类型: " + type);
        }
        redisUtil.setCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + receiverId, vo, 3, TimeUnit.DAYS);

        return update;

    }

    /**
     * @Description: 未读消息
     * @Param: []
     * @return: com.example.communityserver.entity.response.UnreadCountByTypeVo
     * @Author: DongGuo
     */

    @Override
    public UnreadCountByTypeVo getUnreadCountByType() {
        // TODO: 2025/7/31 将类型拆分开 ，将redis存储时间提取出来
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo != null) {
            redisUtil.expire(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), 3, TimeUnit.DAYS);
            return vo;
        }
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        // like
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.ARTICLE_LIKE).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long like = notificationMapper.selectCount(queryWrapper);
        // comment
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.COMMENT).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long comment = notificationMapper.selectCount(queryWrapper);
        // follow
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.FOLLOW).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long follow = notificationMapper.selectCount(queryWrapper);
        // system
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.SYSTEM).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long system = notificationMapper.selectCount(queryWrapper);
        // favorite
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.FAVORITE).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long favorite = notificationMapper.selectCount(queryWrapper);
        // reply
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.REPLY).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long reply = notificationMapper.selectCount(queryWrapper);
        // favoriteArticle
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.FAVORITE_ARTICLE).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long favoriteArticle = notificationMapper.selectCount(queryWrapper);
        // privateMessages
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId()).eq(Notification::getType, ActiveTypeEnum.PRIVATE_MESSAGES).eq(Notification::getIsRead, 0).eq(Notification::getIsDel, 0);
        Long privateMessages = notificationMapper.selectCount(queryWrapper);
        vo = new UnreadCountByTypeVo();
        vo.setComment(comment);
        vo.setLike(like);
        vo.setFavorite(favorite);
        vo.setReply(reply);
        vo.setFollow(follow);
        vo.setSystem(system);
        vo.setFavoriteArticle(favoriteArticle);
        vo.setPrivateMessages(privateMessages);
        System.out.println(vo);
        redisUtil.setCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), vo, 3, TimeUnit.DAYS);
        return vo;
    }
}

package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.MarkAsReadParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.UnreadCountByTypeVo;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.service.ILikesService;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 通知
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/

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

    @Override
    public IPage<NotificationListVo> getNotifications(GetNotificationsParam param) {
        if (param.getType() != null) {
            param.setType(param.getType().toUpperCase());
        }
        assert param.getType() != null;
        if (param.getType().equals("LIKE")) {
            param.setType("ARTICLE_LIKE");
            return likesService.getArticleLikeNotificationsVo(param);
        } else if (param.getType().equals("COMMENT")) {
            return commentService.getCommentNotificationsVo(param);
        }
        return null;
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
        updateWrapper.in(Notification::getNotificationId, param.getIds())
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0)
                .eq(Notification::getType, param.getType().toUpperCase())
                .set(Notification::getIsRead, 1);
        int update = notificationMapper.update(null, updateWrapper);

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo == null) {
            return update;
        }
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, param.getType().toUpperCase())
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
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
        updateWrapper.in(Notification::getNotificationId, param.getIds())
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .setSql("is_del = 1 - is_del");

        int update = notificationMapper.update(null, updateWrapper);
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo == null) {
            return update;
        }

        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, param.getType().toUpperCase())
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
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
        queryWrapper.eq(Notification::getType, type)
                .eq(Notification::getSenderId, SecurityUtils.getLoginUserId())
                .eq(Notification::getContentId, contentId);
        int update = notificationMapper.delete(queryWrapper);
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + receiverId);

        if (vo == null) {
            return update;
        }
        queryWrapper.clear();
        queryWrapper
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, type)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);

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
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.ARTICLE_LIKE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long like = notificationMapper.selectCount(queryWrapper);
        // comment
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.COMMENT)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long comment = notificationMapper.selectCount(queryWrapper);
        // follow
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.FOLLOW)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long follow = notificationMapper.selectCount(queryWrapper);
        // system
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.SYSTEM)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long system = notificationMapper.selectCount(queryWrapper);
        // favorite
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.FAVORITE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long favorite = notificationMapper.selectCount(queryWrapper);
        // reply
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.REPLY)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long reply = notificationMapper.selectCount(queryWrapper);
        // favoriteArticle
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.FAVORITE_ARTICLE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long favoriteArticle = notificationMapper.selectCount(queryWrapper);
        // privateMessages
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, ActiveTypeEnum.PRIVATE_MESSAGES)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
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

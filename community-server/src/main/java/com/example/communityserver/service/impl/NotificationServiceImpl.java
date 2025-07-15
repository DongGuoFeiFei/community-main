package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.MarkAsReadParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.UnreadCountByTypeVo;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
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
    private RedisUtil redisUtil;

    @Override
    public IPage<NotificationListVo> getNotifications(GetNotificationsParam param) {
        Page<Notification> page = new Page<>(param.getPage(), param.getSize());
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        if (param.getType() != null) {
            param.setType(param.getType().toUpperCase());
        }
        queryWrapper
                .eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(param.getType() != null, Notification::getType, param.getType())
                .eq(param.getIsRead() != null, Notification::getIsRead, Boolean.TRUE.equals(param.getIsRead()) ? 1 : 0)
                .eq(Notification::getIsDel, 0)
                .orderByAsc(Notification::getIsRead)
                .orderByDesc(Notification::getCreatedAt);

        Page<Notification> entityPage = notificationMapper.selectPage(page, queryWrapper);
        IPage<NotificationListVo> voPage;
        voPage = entityPage.convert(entity -> {
            NotificationListVo vo = new NotificationListVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setType(entity.getType().getLabel());
            vo.setColor(entity.getType().getColor());
            vo.setIsRead(entity.getIsRead() != null && entity.getIsRead() == 1);
            return vo;
        });

        return voPage;
    }

    @Transactional
    @Override
    public Integer markAsRead(MarkAsReadParam param) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
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

    @Override
    public Integer deleteNotification(NotificationTypeEnum type, Long sonSourceId) {
        if (type == null) {
            return -1;
        }
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getType, type)
                .eq(sonSourceId != null, Notification::getSenderId, sonSourceId)
                .setSql("is_del = 1 - is_del");
        int update = notificationMapper.update(null, updateWrapper);
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());

        if (vo == null) {
            return update;
        }

        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
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
        redisUtil.setCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), vo, 3, TimeUnit.DAYS);

        return update;

    }

    @Override
    public UnreadCountByTypeVo getUnreadCountByType() {
        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo != null) {
            redisUtil.expire(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId(), 3, TimeUnit.DAYS);
            return vo;
        }
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        // like
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.LIKE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long like = notificationMapper.selectCount(queryWrapper);
        // comment
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.COMMENT)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long comment = notificationMapper.selectCount(queryWrapper);
        // follow
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.FOLLOW)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long follow = notificationMapper.selectCount(queryWrapper);
        // system
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.SYSTEM)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long system = notificationMapper.selectCount(queryWrapper);
        // favorite
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.FAVORITE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long favorite = notificationMapper.selectCount(queryWrapper);
        // reply
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.REPLY)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long reply = notificationMapper.selectCount(queryWrapper);
        // favoriteArticle
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.FAVORITE_ARTICLE)
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        Long favoriteArticle = notificationMapper.selectCount(queryWrapper);
        // privateMessages
        queryWrapper.clear();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getType, NotificationTypeEnum.PRIVATE_MESSAGES)
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

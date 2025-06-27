package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.mapper.NotificationEntityMapper;
import com.example.communityserver.service.INotificationEntityService;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 通知
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/

@Service
public class NotificationEntityServiceImpl extends ServiceImpl<NotificationEntityMapper, NotificationEntity> implements INotificationEntityService {
    @Autowired
    private NotificationEntityMapper notificationEntityMapper;

    @Override
    public IPage<NotificationListVo> getNotifications(GetNotificationsParam param) {
        Page<NotificationEntity> page = new Page<>(param.getPage(), param.getSize());
        LambdaQueryWrapper<NotificationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(NotificationEntity::getUserId, SecurityUtils.getLoginUserId())
                .eq(param.getType() != null, NotificationEntity::getType, param.getType())
                .eq(param.getIsRead() != null, NotificationEntity::getIsRead, Boolean.TRUE.equals(param.getIsRead()) ? 1 : 0);

        Page<NotificationEntity> entityPage = notificationEntityMapper.selectPage(page, queryWrapper);
        IPage<NotificationListVo> voPage;
        voPage = entityPage.convert(entity -> {
            NotificationListVo vo = new NotificationListVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setIsRead(entity.getIsRead() != null && entity.getIsRead() == 1);
            return vo;
        });

        return voPage;
    }

    @Override
    public Integer markAsRead(IdsListParam param) {
        LambdaUpdateWrapper<NotificationEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(NotificationEntity::getNotificationId, param.getIds())
                .eq(NotificationEntity::getUserId, SecurityUtils.getLoginUserId())
                .eq(NotificationEntity::getIsRead, 0)
                .set(NotificationEntity::getIsRead, 1);
        return notificationEntityMapper.update(null, updateWrapper);
    }

    @Override
    public Integer deleteNotifications(IdsListParam param) {
        LambdaUpdateWrapper<NotificationEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(NotificationEntity::getNotificationId, param.getIds())
                .eq(NotificationEntity::getUserId, SecurityUtils.getLoginUserId())
                .setSql("is_del = 1 - is_del");
        return notificationEntityMapper.update(null, updateWrapper);
    }
}

package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.response.NotificationListVo;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/


public interface INotificationEntityService extends IService<NotificationEntity> {

    IPage<NotificationListVo> getNotifications(GetNotificationsParam param);

    Integer markAsRead(IdsListParam param);

    Integer deleteNotifications(IdsListParam param);

    Integer deleteNotification(NotificationTypeEnum type, Long sonSourceId);
}

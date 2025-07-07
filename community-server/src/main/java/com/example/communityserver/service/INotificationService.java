package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.request.MarkAsReadParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.UnreadCountByTypeVo;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/


public interface INotificationService extends IService<Notification> {

    IPage<NotificationListVo> getNotifications(GetNotificationsParam param);

    Integer markAsRead(MarkAsReadParam param);

    Integer deleteNotifications(MarkAsReadParam param);

    Integer deleteNotification(NotificationTypeEnum type, Long sonSourceId);

    UnreadCountByTypeVo getUnreadCountByType();

}

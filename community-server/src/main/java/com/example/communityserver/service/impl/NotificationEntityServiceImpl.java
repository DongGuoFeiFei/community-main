package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.NotificationEntity;
import com.example.communityserver.mapper.NotificationEntityMapper;
import com.example.communityserver.service.INotificationEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  通知
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-05
 **/

@Service
public class NotificationEntityServiceImpl extends ServiceImpl<NotificationEntityMapper, NotificationEntity> implements INotificationEntityService {
    @Autowired
    private NotificationEntityMapper mapper;
}

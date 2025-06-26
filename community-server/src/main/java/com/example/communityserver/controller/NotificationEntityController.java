package com.example.communityserver.controller;

import com.example.communityserver.service.INotificationEntityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 通知管理
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/

@Api(tags = "通知管理")
@RestController
@RequestMapping("/notifications")
public class NotificationEntityController {
    @Autowired
    private INotificationEntityService notificationEntityService;


}

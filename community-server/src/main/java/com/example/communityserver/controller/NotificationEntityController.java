package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.service.INotificationEntityService;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    // TODO: 2025/6/30 消息通知接收将不同类别的通知分开接收
    @ApiOperation("获取通知列表接口")
    @GetMapping
    public Result<Result.PageData<NotificationListVo>> getNotifications(@Valid GetNotificationsParam param) {
        IPage<NotificationListVo> page = notificationEntityService.getNotifications(param);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("标记通知为已读接口")
    @PutMapping("/read")
    public Result<Void> markAsRead(@RequestBody @Valid IdsListParam param) {
        Integer update = notificationEntityService.markAsRead(param);
        return update > 0 ? Result.success() : Result.error();
    }

    @ApiOperation("删除通知接口")
    @DeleteMapping
    public Result<Void> deleteNotifications(@RequestBody @Valid IdsListParam param) {
        Integer del = notificationEntityService.deleteNotifications(param);
        return del > 0 ? Result.success() : Result.error();
    }

    @ApiOperation("获取未读通知数量接口")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount() {
        LambdaQueryWrapper<NotificationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NotificationEntity::getUserId, SecurityUtils.getLoginUserId())
                .eq(NotificationEntity::getIsRead, 0);
        long count = notificationEntityService.count(queryWrapper);
        return Result.success(count);
    }

}

package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.request.MarkAsReadParam;
import com.example.communityserver.entity.response.NotificationListVo;
import com.example.communityserver.entity.response.UnreadCountByTypeVo;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.security.util.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Api(tags = "通知管理")
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private INotificationService notificationEntityService;

    @Autowired
    private RedisUtil redisUtil;

    // TODO: 2025/6/30 消息通知接收将不同类别的通知分开接收
    @ApiOperation("获取通知列表")
    @GetMapping
    public Result<Result.PageData<NotificationListVo>> getNotifications(@Valid GetNotificationsParam param) {
        IPage<NotificationListVo> page = notificationEntityService.getNotifications(param);
        log.info("{}", page.getRecords());
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("标记通知为已读")
    @PutMapping("/read")
    public Result<Void> markAsRead(@RequestBody @Valid MarkAsReadParam param) {
        Integer update = notificationEntityService.markAsRead(param);
        return update > 0 ? Result.success() : Result.error();
    }

    @ApiOperation("删除通知")
    @DeleteMapping
    public Result<Void> deleteNotifications(@RequestBody @Valid MarkAsReadParam param) {
        Integer del = notificationEntityService.deleteNotifications(param);
        return del > 0 ? Result.success() : Result.error();
    }

    @ApiOperation("获取未读通知数量")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount() {

        UnreadCountByTypeVo vo = redisUtil.getCacheObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        if (vo != null) {
            return Result.success(
                    vo.getComment() + vo.getLike()
                            + vo.getFollow() + vo.getFavorite()
                            + vo.getFavorite() + vo.getFavoriteArticle()
                            + vo.getPrivateMessages() + vo.getReply());
        }
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0);
        long count = notificationEntityService.count(queryWrapper);
        return Result.success(count);
    }

    @ApiOperation("获取各类型未读数量统计")
    @GetMapping("/unread-count-by-type")
    public Result<UnreadCountByTypeVo> getUnreadCountByType() {
        UnreadCountByTypeVo count = notificationEntityService.getUnreadCountByType();
        return Result.success(count);
    }

    @ApiOperation("标记全部为已读")
    @PostMapping("/mark-all-read")
    public Result<Void> markAllAsRead() {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getUserId, SecurityUtils.getLoginUserId())
                .eq(Notification::getIsRead, 0)
                .eq(Notification::getIsDel, 0)
                .set(Notification::getIsRead, 1);
        notificationEntityService.update(updateWrapper);
        redisUtil.deleteObject(CacheKeyConstants.UNREAD_TYPE_VO_COUNT + SecurityUtils.getLoginUserId());
        return Result.success();
    }
}

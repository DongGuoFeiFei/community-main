package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.Likes;
import com.example.communityserver.entity.request.GetNotificationsParam;
import com.example.communityserver.entity.response.NotificationListVo;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/


public interface ILikesService extends IService<Likes> {

    IPage<NotificationListVo> getArticleLikeNotificationsVo(GetNotificationsParam param);
}

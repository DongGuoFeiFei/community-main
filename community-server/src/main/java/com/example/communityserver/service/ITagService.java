package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.entity.model.Tag;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/


public interface ITagService extends IService<Tag> {
    List<Tag> getPostTags(Long postId);
}

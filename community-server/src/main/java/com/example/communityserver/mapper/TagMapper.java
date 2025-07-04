package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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


public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getPostTags(Long postId);
}

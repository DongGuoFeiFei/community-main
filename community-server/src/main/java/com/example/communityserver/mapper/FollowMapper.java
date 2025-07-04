package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Follow;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-03
 **/


public interface FollowMapper extends BaseMapper<Follow> {

    Long countFollowers(Long id);

    Long countFollowing(Long id);
}

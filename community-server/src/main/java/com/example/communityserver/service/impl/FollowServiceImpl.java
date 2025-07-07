package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.Follow;
import com.example.communityserver.mapper.FollowMapper;
import com.example.communityserver.service.IFollowService;
import com.example.communityserver.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-03
 **/

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public Long countFollowers(Long id) {
        Long followerCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWER_COUNT + id, Long.class);
        if (followerCount == null) {
            followerCount = followMapper.countFollowers(id);
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWER_COUNT + id, followerCount, 3, TimeUnit.DAYS);

        return followerCount;
    }

    @Override
    public Long countFollowing(Long id) {
        Long followingCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWING_COUNT + id, Long.class);
        if (followingCount == null) {
            followingCount = followMapper.countFollowing(id);
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWING_COUNT + id, followingCount, 3, TimeUnit.DAYS);
        return followingCount;
    }
}

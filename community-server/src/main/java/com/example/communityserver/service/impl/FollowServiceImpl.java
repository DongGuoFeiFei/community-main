package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Follow;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.response.FollowVo;
import com.example.communityserver.mapper.FollowMapper;
import com.example.communityserver.service.IFollowService;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    private INotificationService notificationService;


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

    @Override
    public Boolean followAuthor(Long id) {
        Follow follow = new Follow();
        follow.setFollowerId(id);
        follow.setFollowingId(SecurityUtils.getLoginUserId());
        int insert = followMapper.insert(follow);
        if (insert > 0) {
            Notification notification = new Notification(id, ActiveTypeEnum.FOLLOW, follow.getId(), SecurityUtils.getLoginUserId());
            notificationService.save(notification);
        }
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWING_COUNT + id);
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWER_COUNT + SecurityUtils.getLoginUserId());
        return insert > 0;
    }

    @Override
    public Boolean delFollowAuthor(Long id) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getFollowerId, id)
                .eq(Follow::getFollowingId, SecurityUtils.getLoginUserId());
        Follow follow = followMapper.selectOne(queryWrapper);
        if (follow == null) {
            return false;
        }
        // 删除通知
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getContentId, follow.getId())
                .eq(Notification::getType, ActiveTypeEnum.FOLLOW)
                .eq(Notification::getSenderId, SecurityUtils.getLoginUserId());
        ;
        notificationService.remove(wrapper);
        followMapper.deleteById(follow.getId());
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWING_COUNT + id);
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWER_COUNT + SecurityUtils.getLoginUserId());
        return true;
    }

    @Override
    public Boolean isFollowing(Long id) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getFollowerId, id)
                .eq(Follow::getFollowingId, SecurityUtils.getLoginUserId());
        Follow follow = followMapper.selectOne(queryWrapper);
        return follow != null;
    }

    @Override
    public List<FollowVo> getFollowingList(Long userId) {
        List<FollowVo> followingList = followMapper.getFollowingList(userId, SecurityUtils.getLoginUserId());
        followingList.forEach(followVo -> {
            followVo.setAvatar(SystemConstants.BASIC_URL + followVo.getAvatar());
        });
        return followingList;
    }

    @Override
    public List<FollowVo> getFollowerList(Long userId) {
        List<FollowVo> followerList = followMapper.getFollowerList(userId, SecurityUtils.getLoginUserId());
        followerList.forEach(followVo -> {
            followVo.setAvatar(SystemConstants.BASIC_URL + followVo.getAvatar());
        });
        return followerList;
    }

    @Override
    public Boolean removeFan(Long fanId) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getFollowingId, fanId)
                .eq(Follow::getFollowerId, SecurityUtils.getLoginUserId());
        Follow follow = followMapper.selectOne(queryWrapper);
        if (follow == null) {
            return false;
        }
        // 删除通知
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getContentId, follow.getId())
                .eq(Notification::getType, ActiveTypeEnum.FOLLOW)
                .eq(Notification::getSenderId, follow.getFollowingId());
        notificationService.remove(wrapper);
        followMapper.deleteById(follow.getId());
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWING_COUNT + SecurityUtils.getLoginUserId());
        redisUtil.deleteObject(CacheKeyConstants.USER_FOLLOWER_COUNT + fanId);
        return true;

    }
}

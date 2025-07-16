package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.Follow;
import com.example.communityserver.entity.response.FollowVo;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-03
 **/


public interface IFollowService extends IService<Follow> {
    Long countFollowers(Long id);

    Long countFollowing(Long id);

    Boolean followAuthor(Long id);

    Boolean delFollowAuthor(Long id);

    Boolean isFollowing(Long id);

    List<FollowVo> getFollowingList(Long userId);

    List<FollowVo> getFollowerList(Long userId);
}

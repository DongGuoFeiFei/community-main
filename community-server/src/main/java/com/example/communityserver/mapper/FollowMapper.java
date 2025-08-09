package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Follow;
import com.example.communityserver.entity.response.FollowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-03
 **/


public interface FollowMapper extends BaseMapper<Follow> {

    Long countFollowers(Long id);

    Long countFollowing(Long id);

    List<FollowVo> getFollowingList(@Param("userId") Long userId, @Param("loginUserId") Long loginUserId);

    List<FollowVo> getFollowerList(@Param("userId") Long userId, @Param("loginUserId") Long loginUserId);

}

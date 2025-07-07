package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.entity.model.Follow;
import com.example.communityserver.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;

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
}

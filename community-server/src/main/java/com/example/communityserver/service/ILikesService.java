package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.Likes;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/


public interface ILikesService extends IService<Likes> {
    boolean addLike(Long id);
}

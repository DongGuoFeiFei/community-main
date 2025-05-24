package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.Likes;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.service.ILikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/

@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements ILikesService {
    @Autowired
    private LikesMapper likesMapper;
}

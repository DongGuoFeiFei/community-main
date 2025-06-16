package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.Likes;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.service.ILikesService;
import com.example.communityserver.utils.SecurityUtils;
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

    @Override
    public boolean addLike(Long id) {
        // 查询表格中是否已经拥有了该点赞数据，已有数据——修改数据；未有数据——添加数据
        LambdaUpdateWrapper<Likes> updateWrapper = new LambdaUpdateWrapper<>();
        // type user_id target_id
        updateWrapper.eq(Likes::getTargetId, id)
                .eq(Likes::getType, "article")
                .eq(Likes::getUserId, SecurityUtils.getLoginUserId())
                .setSql("is_like = 1 - is_like");
        int update = likesMapper.update(null, updateWrapper);
        if (update > 0) {
            // 更新成功
            return true;
        } else {
            // 更新失败，插入
            Likes likes = new Likes();
            likes.setIsLike(1);
            likes.setType("article");
            likes.setUserId(SecurityUtils.getLoginUserId());
            likes.setTargetId(id);
            return likesMapper.insert(likes) > 0;
        }
    }
}

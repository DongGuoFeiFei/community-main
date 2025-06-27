package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.enums.NotificationTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Likes;
import com.example.communityserver.entity.model.NotificationEntity;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.LikesMapper;
import com.example.communityserver.mapper.NotificationEntityMapper;
import com.example.communityserver.service.ILikesService;
import com.example.communityserver.service.INotificationEntityService;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private NotificationEntityMapper notificationEntityMapper;
    @Autowired
    private INotificationEntityService notificationEntityService;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional
    public boolean addLike(Long id) {
        // 查询表格中是否已经拥有了该点赞数据，已有数据——修改数据；未有数据——添加数据
        LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();
        // type user_id target_id
        queryWrapper.eq(Likes::getTargetId, id)
                .eq(Likes::getType, NotificationTypeEnum.ARTICLE)
                .eq(Likes::getUserId, SecurityUtils.getLoginUserId());
        Likes likes = likesMapper.selectOne(queryWrapper);
        if (likes != null) {
            // 更新成功
            LambdaUpdateWrapper<Likes> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.setSql("is_like = 1 - is_like");
            likesMapper.update(null, updateWrapper);
            Integer del = notificationEntityService.deleteNotification(likes.getType(), likes.getLikeId());
            return true;
        } else {
            likes = new Likes();
            // 更新失败 -- 插入
            likes.setIsLike(1);
            likes.setType(NotificationTypeEnum.ARTICLE);
            likes.setUserId(SecurityUtils.getLoginUserId());
            likes.setTargetId(id);
            if (likesMapper.insert(likes) > 0) {
                NotificationEntity notificationEntity = new NotificationEntity();
                notificationEntity.setType(NotificationTypeEnum.LIKE);
                notificationEntity.setParentSourceId(likes.getTargetId());
                notificationEntity.setSonSourceId(likes.getLikeId());
                Article article = articleMapper.selectById(likes.getTargetId());
                notificationEntity.setUserId(article.getUserId());
                notificationEntityMapper.insert(notificationEntity);
                return true;
            }
        }
        return false;
    }
}

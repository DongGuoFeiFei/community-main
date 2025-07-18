package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.request.AddFavoriteArticle;
import com.example.communityserver.entity.request.GetUserFavoListParam;
import com.example.communityserver.entity.request.MoveFavoriteDto;
import com.example.communityserver.entity.response.FavArticleVo;
import com.example.communityserver.entity.response.MoveFavoriteVo;
import com.example.communityserver.entity.response.UserFavoListVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.mapper.UserFavoriteMapper;
import com.example.communityserver.mapping.FavoriteMapping;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.service.IUserFavoriteService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Service
public class UserUserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite> implements IUserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private INotificationService notificationEntityService;
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public FavArticleVo addFavArticle(AddFavoriteArticle param) {
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setTargetId(param.getArticleId());
        userFavorite.setFolderId(param.getFolderId());
        userFavorite.setUserId(SecurityUtils.getLoginUserId());
        userFavorite.setType(ActiveTypeEnum.FAVORITE_ARTICLE);
        int insert = userFavoriteMapper.insert(userFavorite);
        FavArticleVo favArticleVo = new FavArticleVo();
        if (insert > 0) {
            BeanUtils.copyProperties(userFavorite, favArticleVo);
            favArticleVo.setArticleId(userFavorite.getTargetId());

            // 插入通知数据
            Notification notification = new Notification();
            Article article = articleMapper.selectById(param.getArticleId());
            notification.setUserId(article.getUserId());
            notification.setType(ActiveTypeEnum.FAVORITE_ARTICLE);
            notification.setContentId(userFavorite.getFavoriteId());
            notification.setSenderId(SecurityUtils.getLoginUserId());
            notificationEntityService.save(notification);

        } else {
            favArticleVo = null;
        }
        return favArticleVo;
    }

    @Override
    public Page<UserFavoListVo> getUserFavoListVo(GetUserFavoListParam param) {
        Page<UserFavoListVo> page = new Page<>(param.getPage(), param.getSize());
        // TODO: 2025/6/22 连接的表格过多，优化sql，分成多次查询，代码组装数据
        return userFavoriteMapper.selectFavoList(page, param, SecurityUtils.getLoginUserId());
    }

    @Override
    public MoveFavoriteVo moveFavoriteVoResult(MoveFavoriteDto dto) {
        // 判断是否存在——删除原有——建立新数据
        MoveFavoriteVo vo = new MoveFavoriteVo();
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SecurityUtils.getLoginUserId() != null, UserFavorite::getUserId, SecurityUtils.getLoginUserId())
                .eq(dto.getArticleId() != null, UserFavorite::getTargetId, dto.getArticleId())
                .eq(dto.getActiveFolderId() != null, UserFavorite::getFolderId, dto.getActiveFolderId());
        UserFavorite userFavorite = userFavoriteMapper.selectOne(queryWrapper);
        if (userFavorite != null) {
            int delete = userFavoriteMapper.delete(queryWrapper);
            if (delete > 0) {
                userFavorite.setFavoriteId(null);
                userFavorite.setFolderId(dto.getFolderId());
                userFavorite.setType(ActiveTypeEnum.FAVORITE_ARTICLE);
                int insert = userFavoriteMapper.insert(userFavorite);
                if (insert > 0) {
                    FavoriteMapping.INSTANCE.updateMoveVoFromUserFavorite(userFavorite, vo);
                } else {
                    vo = null;
                }
            }
        }
        return vo;
    }

    @Override
    @Transactional
    public boolean removeFavorite(Long articleId) {
        // TODO: 2025/7/18 目前删除收藏文章未传入文件夹id，就是说用户只能收藏一次，不能收藏在多个文件夹中
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(articleId != null, UserFavorite::getTargetId, articleId)
                .eq(UserFavorite::getType,ActiveTypeEnum.FAVORITE_ARTICLE)
                .eq(SecurityUtils.getLoginUserId() != null, UserFavorite::getUserId, SecurityUtils.getLoginUserId());
        UserFavorite userFavorite = userFavoriteMapper.selectOne(queryWrapper);
        userFavoriteMapper.deleteById(userFavorite.getFavoriteId());
        Article article = articleMapper.selectById(articleId);
        Integer integer = notificationEntityService.deleteNotification(userFavorite.getType(), userFavorite.getFavoriteId(),article.getUserId());
        redisUtil.deleteObject(CacheKeyConstants.ARTICLE_FAVORITE_COUNT + articleId);
        return integer > 0;
    }
}

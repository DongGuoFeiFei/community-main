package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.request.AddFavoriteArticle;
import com.example.communityserver.entity.request.GetUserFavoListParam;
import com.example.communityserver.entity.request.MoveFavoriteDto;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.response.FavArticleVo;
import com.example.communityserver.entity.response.MoveFavoriteVo;
import com.example.communityserver.entity.response.UserFavoListVo;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/


public interface IUserFavoriteService extends IService<UserFavorite> {
    FavArticleVo addFavArticle(AddFavoriteArticle param);

    Page<UserFavoListVo> getUserFavoListVo(GetUserFavoListParam param);

    MoveFavoriteVo moveFavoriteVoResult(MoveFavoriteDto dto);

    boolean removeFavorite(Long articleId);
}

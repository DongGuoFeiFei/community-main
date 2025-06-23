package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.dto.AddFavoriteArticle;
import com.example.communityserver.entity.dto.GetUserFavoListParam;
import com.example.communityserver.entity.dto.MoveFavoriteDto;
import com.example.communityserver.entity.po.UserFavorite;
import com.example.communityserver.entity.vo.FavArticleVo;
import com.example.communityserver.entity.vo.MoveFavoriteVo;
import com.example.communityserver.entity.vo.UserFavoListVo;

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

}

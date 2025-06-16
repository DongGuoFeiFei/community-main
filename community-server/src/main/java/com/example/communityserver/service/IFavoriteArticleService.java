package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.dto.AddFavoriteArticle;
import com.example.communityserver.entity.po.FavoriteArticle;
import com.example.communityserver.entity.vo.FavArticleVo;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/


public interface IFavoriteArticleService extends IService<FavoriteArticle> {
    FavArticleVo addFavArticle(AddFavoriteArticle param);
}

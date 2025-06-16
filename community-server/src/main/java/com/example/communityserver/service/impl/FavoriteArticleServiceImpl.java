package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.dto.AddFavoriteArticle;
import com.example.communityserver.entity.po.FavoriteArticle;
import com.example.communityserver.entity.vo.FavArticleVo;
import com.example.communityserver.mapper.FavoriteArticleMapper;
import com.example.communityserver.service.IFavoriteArticleService;
import com.example.communityserver.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Service
public class FavoriteArticleServiceImpl extends ServiceImpl<FavoriteArticleMapper, FavoriteArticle> implements IFavoriteArticleService {
    @Autowired
    private FavoriteArticleMapper favoriteArticleMapper;

    @Override
    public FavArticleVo addFavArticle(AddFavoriteArticle param) {
        FavoriteArticle favoriteArticle = new FavoriteArticle();
        favoriteArticle.setArticleId(param.getArticleId());
        favoriteArticle.setFolderId(param.getFolderId());
        favoriteArticle.setUserId(SecurityUtils.getLoginUserId());
        int insert = favoriteArticleMapper.insert(favoriteArticle);
        FavArticleVo favArticleVo = new FavArticleVo();
        if (insert>0){
            BeanUtils.copyProperties(favoriteArticle,favArticleVo);
        }else {
            favArticleVo = null;
        }
        return favArticleVo;
    }
}

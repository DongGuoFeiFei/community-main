package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.request.SearchParam;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import com.example.communityserver.entity.response.ArticleListVo;
import com.example.communityserver.entity.response.EditorArticlesVo;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/


public interface IArticleService extends IService<Article> {


    List<ArticleCardVo> getPostsCardVoById(Long id);

    boolean addArticle(AddArticleDto dto);


    boolean delById(Long id);

    EditorArticlesVo getEditorArticleDtl(Long id);

    ArticleDtlVo getArticleDtlVo(Long id);

    Page<ArticleCardVo> getPostsCardVoList(SearchParam param);

    Page<ArticleListVo> getArticleList(GetArticleListDto dto);
}

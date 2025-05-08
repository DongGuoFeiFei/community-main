package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.dto.AddArticleDto;
import com.example.communityserver.entity.dto.MarkdownDto;
import com.example.communityserver.entity.po.Article;
import com.example.communityserver.entity.vo.ArticleCardVo;

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

    List<ArticleCardVo> getPostsCardVoList(String title);

    List<ArticleCardVo> getPostsCardVoById(Long id);

    boolean addArticle(AddArticleDto dto);
}

package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.request.FetchPostsParam;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/


public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleCardVo> getPostsCardVoById(Long id);


    EditorArticlesVo getEditorArticleDtl(@Param("id") Long id, @Param("loginUserId") Long loginUserId);

    ArticleDtlVo getArticleDtlVo(Long id);

    Page<ArticleCardVo> getPostsCardVoList(Page<ArticleCardVo> page, @Param("param") FetchPostsParam param, @Param("categoryIds") List<Long> categoryIds);

    Page<ArticleListVo> getArticleList(Page<ArticleListVo> page, @Param("dto") GetArticleListDto dto, @Param("loginUserId") Long loginUserId);

    Long countByUser(Long id);

    List<UserPostVo> getUserPosts(Long userId);

    List<UserPostVo> getUserFavorites(@Param("userId") Long userId, @Param("article") ActiveTypeEnum article);
}

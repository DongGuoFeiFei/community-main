package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import com.example.communityserver.entity.response.ArticleListVo;
import com.example.communityserver.entity.response.EditorArticlesVo;
import org.apache.ibatis.annotations.Param;

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

    List<ArticleCardVo> getPostsCardVoList(String title);

    List<ArticleCardVo> getPostsCardVoById(Long id);

    List<ArticleListVo> getArticleList(@Param("title") String title, @Param("status") Integer status, @Param("sortField") String sortField, @Param("isAsc") Boolean isAsc);

    EditorArticlesVo getEditorArticleDtl(@Param("id") Long id, @Param("loginUserId") Long loginUserId);

    ArticleDtlVo getArticleDtlVo(Long id);
}

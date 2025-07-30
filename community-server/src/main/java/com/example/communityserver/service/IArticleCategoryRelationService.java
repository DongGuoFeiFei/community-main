package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.ArticleCategoryRelation;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/


public interface IArticleCategoryRelationService extends IService<ArticleCategoryRelation> {
    List<ArticleCategoryRelation> getArticleCategoriesByArticleId(Long articleId);

    int batchInsert(List<Long> categoryIds, Long articleId);

    boolean delACRelation(Long articleId);
}

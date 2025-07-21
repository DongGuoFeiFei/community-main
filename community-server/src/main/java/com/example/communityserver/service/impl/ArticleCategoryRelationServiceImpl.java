package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ArticleCategoryRelation;
import com.example.communityserver.mapper.ArticleCategoryRelationMapper;
import com.example.communityserver.mapper.ContentCategoryMapper;
import com.example.communityserver.service.IArticleCategoryRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/

@Service
public class ArticleCategoryRelationServiceImpl extends ServiceImpl<ArticleCategoryRelationMapper, ArticleCategoryRelation> implements IArticleCategoryRelationService {
    @Autowired
    private ArticleCategoryRelationMapper articleCategoryRelationMapper;

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Override
    public List<ArticleCategoryRelation> getArticleCategoriesByArticleId(Long articleId) {
        // 互获取文章对应的类别id
        LambdaQueryWrapper<ArticleCategoryRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleCategoryRelation::getArticleId, articleId);
        return articleCategoryRelationMapper.selectList(queryWrapper);
    }
}

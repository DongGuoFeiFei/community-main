package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ArticleCategoryRelation;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.model.Tag;
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

    @Override
    public int batchInsert(List<Long> categoryIds, Long articleId) {

        if (categoryIds == null || categoryIds.isEmpty() || articleId == null) {
            return 0;
        }
        // 次数加一
        LambdaUpdateWrapper<ContentCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ContentCategory::getId, categoryIds)
                .setSql("article_count = article_count + 1");
        contentCategoryMapper.update(updateWrapper);
        return articleCategoryRelationMapper.batchInsert(categoryIds, articleId);

    }

    @Override
    public boolean delACRelation(List<Long> categoryIds, Long articleId) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return false;
        }
        System.out.println(categoryIds);
        return articleCategoryRelationMapper.delACRelation(categoryIds,articleId);

    }
}

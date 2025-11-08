package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.ArticleCategoryRelation;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.request.AddCategoryParam;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.entity.response.ContentCategoryTree;
import com.example.communityserver.mapper.ArticleCategoryRelationMapper;
import com.example.communityserver.mapper.ContentCategoryMapper;
import com.example.communityserver.service.IContentCategoryService;
import com.example.communityserver.utils.common.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/
@Slf4j
@Service
public class ContentCategoryServiceImpl extends ServiceImpl<ContentCategoryMapper, ContentCategory> implements IContentCategoryService {
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Autowired
    private ArticleCategoryRelationMapper articleCategoryRelationMapper;

    @Override
    public List<ContentCategoryTree> getCategoryTrees() {
        // TODO: 2025/11/8  按照文章的热度进行排名，但是不能显示文章的热度
        List<ContentCategoryTree> categoryTrees = contentCategoryMapper.getCategoryTrees();
        return null == categoryTrees ? Collections.emptyList() : categoryTrees;
    }

    @Override
    public IPage<ContentCategory> getCategories(SearchNameStatusParam param) {
        Page<ContentCategory> contentCategoryPage = new Page<>(param.getPage(), param.getSize());
        LambdaQueryWrapper<ContentCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotBlank(param.getKeyword()), ContentCategory::getCategoryName, param.getKeyword()).eq(param.getStatus() != null, ContentCategory::getStatus, param.getStatus());
        return contentCategoryMapper.selectPage(contentCategoryPage, queryWrapper);
    }

    @Override
    public Boolean deleteCategory(Long categoryId) {
        int i = contentCategoryMapper.deleteById(categoryId);
        if (i > 0) {
            LambdaQueryWrapper<ArticleCategoryRelation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleCategoryRelation::getCategoryId, categoryId);
            int delete = articleCategoryRelationMapper.delete(queryWrapper);
            return delete > 0;
        }
        return false;
    }

    @Override
    public Boolean addCategory(AddCategoryParam param) {
        ContentCategory contentCategory = new ContentCategory();
        BeanUtils.copyProperties(param, contentCategory);
        return contentCategoryMapper.insert(contentCategory) > 0;
    }

    @Override
    public Boolean updateCategory(AddCategoryParam param) {
        // 构建更新条件
        LambdaUpdateWrapper<ContentCategory> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContentCategory::getId, param.getId())
                // 只更新非null的字段
                .set(param.getCategoryName() != null, ContentCategory::getCategoryName, param.getCategoryName()).set(param.getCategorySlug() != null, ContentCategory::getCategorySlug, param.getCategorySlug()).set(param.getParentId() != null, ContentCategory::getParentId, param.getParentId()).set(param.getSortOrder() != null, ContentCategory::getSortOrder, param.getSortOrder()).set(param.getStatus() != null, ContentCategory::getStatus, param.getStatus()).set(param.getDescription() != null, ContentCategory::getDescription, param.getDescription()).set(param.getCoverUrl() != null, ContentCategory::getCoverUrl, param.getCoverUrl())
                // 设置更新时间
                .set(ContentCategory::getUpdatedAt, new Date());

        // 执行更新
        return contentCategoryMapper.update(null, updateWrapper) > 0;
    }

}

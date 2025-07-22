package com.example.communityserver.controller;

import com.example.communityserver.entity.model.ArticleCategoryRelation;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.response.ContentCategoryTree;
import com.example.communityserver.service.IArticleCategoryRelationService;
import com.example.communityserver.service.IContentCategoryService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-21
 **/

@Api(tags = "文章分类")
@RestController
@RequestMapping("/category")
public class ContentCategoryController {
    @Autowired
    private IContentCategoryService contentCategoryService;
    @Autowired
    private IArticleCategoryRelationService articleCategoryRelationService;


    @ApiOperation("获取所有分类")
    @GetMapping("/list")
    public Result<List<ContentCategory>> getCategories() {
        List<ContentCategory> list = contentCategoryService.list();
        return Result.success(list);
    }


    @ApiOperation("获取文章的分类")
    @GetMapping("/{articleId}/categories")
    public Result<List<ArticleCategoryRelation>> getArticleCategories(@PathVariable Long articleId) {
        List<ArticleCategoryRelation> list = articleCategoryRelationService.getArticleCategoriesByArticleId(articleId);
        return Result.success(list);
    }

    @ApiOperation("获取所有分类树")
    @GetMapping("/listTree")
    public Result<List<ContentCategoryTree>> getCategoryTrees() {
        List<ContentCategoryTree> list = contentCategoryService.getCategoryTrees();
        return Result.success(list);
    }
}

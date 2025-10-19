package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.model.ArticleCategoryRelation;
import com.example.communityserver.entity.model.ContentCategory;
import com.example.communityserver.entity.request.AddCategoryParam;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.ContentCategoryTree;
import com.example.communityserver.entity.response.UserDelVo;
import com.example.communityserver.security.core.Logical;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IArticleCategoryRelationService;
import com.example.communityserver.service.IContentCategoryService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequiresPermission(api = {"category:list:get"}, role = {"super_admin"})
    public Result<List<ContentCategory>> getCategories() {
        List<ContentCategory> list = contentCategoryService.list();
        return Result.success(list);
    }


    @ApiOperation("获取文章的分类")
    @GetMapping("/{articleId}/categories")
    @RequiresPermission(api = {"category:{articleId}:categories:get"}, role = {"super_admin"})
    public Result<List<ArticleCategoryRelation>> getArticleCategories(@PathVariable Long articleId) {
        List<ArticleCategoryRelation> list = articleCategoryRelationService.getArticleCategoriesByArticleId(articleId);
        return Result.success(list);
    }

    @ApiOperation("获取所有分类树")
    @GetMapping("/listTree")
    @RequiresPermission(api = {"category:listTree:get"}, role = {"super_admin"})
    public Result<List<ContentCategoryTree>> getCategoryTrees() {
        List<ContentCategoryTree> list = contentCategoryService.getCategoryTrees();
        return Result.success(list);
    }

    @ApiOperation("获取分类列表")
    @GetMapping()
    @RequiresPermission(api = {"category:get"}, role = {"super_admin"})
    public Result<Result.PageData<ContentCategory>> getCategories(SearchNameStatusParam param) {
        IPage<ContentCategory> page = contentCategoryService.getCategories(param);

        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("获取分类列表")
    @DeleteMapping("{categoryId}")
    @RequiresPermission(api = {"category:{categoryId}:delete"}, role = {"super_admin"})
    public Result<Void> deleteCategory(@PathVariable Long categoryId) {
        Boolean is = contentCategoryService.deleteCategory(categoryId);
        return is ? Result.success() : Result.error();
    }

    @ApiOperation("添加分类")
    @PostMapping()
    @RequiresPermission(api = {"category:add"}, role = {"super_admin"})
    public Result<Void> addCategory(@RequestBody AddCategoryParam param) {
        Boolean is = contentCategoryService.addCategory(param);
        return is ? Result.success() : Result.error();
    }

    @ApiOperation("更新分类")
    @PutMapping()
    @RequiresPermission(api = {"category:update"}, role = {"super_admin"})
    public Result<Void> updateCategory(@RequestBody AddCategoryParam param) {
        Boolean is = contentCategoryService.updateCategory(param);
        return is ? Result.success() : Result.error();
    }

}

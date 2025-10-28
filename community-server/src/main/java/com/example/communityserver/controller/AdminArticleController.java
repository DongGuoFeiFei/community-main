package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.request.ArticleSearchParam;
import com.example.communityserver.entity.response.AdminArticleListVo;
import com.example.communityserver.core.security.aop.RequiresPermission;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Api(tags = "管理员文章管理")
@RestController
@RequestMapping("admin/article")
public class AdminArticleController {
    @Autowired
    private IArticleService articleService;

    @ApiOperation("获取文章列表")
    @GetMapping("list")
    @RequiresPermission(api = {"admin:article:list:get"}, role = {"super_admin"})
    public Result<Result.PageData<AdminArticleListVo>> getArticleList(ArticleSearchParam param) {
        IPage<AdminArticleListVo> page = articleService.getAdminArticleList(param);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("删除文章")
    @PostMapping("delete/{id}")
    @RequiresPermission(api = {"admin:article:delete:{id}:post"}, role = {"super_admin"})
    public Result<Void> deleteArticle(@PathVariable Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getArticleId, id).set(Article::getIsDel, 0);
        boolean b = articleService.update(updateWrapper);
        return b ? Result.success() : Result.error();
    }

}

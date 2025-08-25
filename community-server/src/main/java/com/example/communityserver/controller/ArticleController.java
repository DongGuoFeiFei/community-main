package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.request.FetchPostsParam;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.request.IdStatusParam;
import com.example.communityserver.entity.response.*;
import com.example.communityserver.mapping.ArticleMapping;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IArticleCategoryRelationService;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.markdown.MarkDownUtils;
import com.example.communityserver.utils.web.Result;
import com.example.communityserver.utils.web.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 帖子 文章 article
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/
@Slf4j
@Api(tags = "帖子内容")
@RestController
@RequestMapping("/posts")
public class ArticleController {
    @Autowired
    private IArticleService postsService;

    @Autowired
    private ITagService tagService;

    @Autowired
    private IArticleCategoryRelationService articleCategoryRelationService;

    // TODO: 2025/6/28 游客能够直接使用查看文章功能
    // TODO: 2025/6/28 权限使用

    @ApiOperation("搜索文章（首页列表）")
    @GetMapping
    public TableDataInfo fetchPosts(FetchPostsParam param) {
        log.info("{}", param);
        Page<ArticleCardVo> page = postsService.getPostsCardVoList(param);
        TableDataInfo tableDataInfo = new TableDataInfo(page.getRecords(), (int) page.getTotal());
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("成功");
        return tableDataInfo;
    }

    @ApiOperation("文章详情(打开一篇文章，阅读文章)")
    @GetMapping("/{id}")
    public Result<ArticleDtlVo> fetchPostsDetail(@PathVariable Long id) {
        // TODO: 2025/7/17 文章被删除时的文章展示
        ArticleDtlVo articleDtlVo = postsService.getArticleDtlVo(id);
        return articleDtlVo != null ? Result.success(articleDtlVo) : Result.error();
    }

    @ApiOperation("编辑文章（获取要编辑文章的数据）")
    @GetMapping("/getArticleDtl/{id}")
    public Result<EditorArticlesVo> getEditorArticleDtl(@PathVariable Long id) {
        // 用户编辑自己的文章，需要进行身份验证，保证是用户本人自己
        EditorArticlesVo articlesVo = postsService.getEditorArticleDtl(id);
        return articlesVo != null ? Result.success(articlesVo) : Result.error("稍后重试");
    }


    // TODO: 2025/7/21 添加文章he更新文章  增加分类数据--->文章查询时，也要增加分类
    @ApiOperation("添加文章")
    @PostMapping("/addArticle")
    public Result<Void> addArticle(@RequestBody AddArticleDto dto) {
        return postsService.addArticle(dto) ? Result.success() : Result.error("失败");
    }

    @ApiOperation("更新文章")
    @PutMapping("/updateArticleDtl/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateArticleDtl(@PathVariable Long id, @RequestBody @Valid AddArticleDto updateDto) {
        log.info("updateDto:{}", updateDto);
        log.info("{}", id);
        Article article = new Article();
        article.setIsDrafts(updateDto.getStatus());
        article.setArticleId(id);
        article.setFileId(updateDto.getFileId());
        article.setContent(updateDto.getContent());
        article.setTitle(updateDto.getTitle());
        boolean b = postsService.updateById(article);
        System.out.println(updateDto.getTagIds());
        boolean delTagArticle = tagService.delTagArticle(updateDto.getTagIds(), article.getArticleId());
        tagService.batchInsert(updateDto.getTagIds(), article.getArticleId());
        boolean delACRelation = articleCategoryRelationService.delACRelation(article.getArticleId());
        log.info("delACRelation:{}", delACRelation);
        int i = articleCategoryRelationService.batchInsert(updateDto.getCategoryIds(), article.getArticleId());
        return i > 0 ? Result.success() : Result.error("失败");
    }

    @ApiOperation("获取文章列表（用户管理）")
    @GetMapping("/getArticleList")
    public TableDataInfo getArticleList(GetArticleListDto dto) {
        Page<ArticleListVo> page = postsService.getArticleList(dto);
        TableDataInfo tableDataInfo = new TableDataInfo(page.getRecords(), (int) page.getTotal());
        tableDataInfo.setMsg("成功");
        tableDataInfo.setCode(200);
        return tableDataInfo;
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        return postsService.delById(id) ? Result.success() : Result.error("失败");
    }

    @ApiOperation("获取文章标签")
    @GetMapping("/{postId}/tags")
    public Result<List<TagVo>> getPostTags(@PathVariable Long postId) {
        List<TagVo> tags = tagService.getPostTags(postId);
        return tags != null ? Result.success(tags) : Result.error();
    }

    @ApiOperation("热门文章")
    @GetMapping("/hotPosts")
    public Result<List<ArticleCardVo>> getHotPosts() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getIsPublic, 1).eq(Article::getIsDel, 1);
        List<Article> list = postsService.list(queryWrapper);
        List<Article> collect = list.stream().limit(5).collect(Collectors.toList());
        collect.forEach(article -> {
            article.setCoverUrl(SystemConstants.BASIC_URL + article.getCoverUrl());
            article.setContent(MarkDownUtils.toPlainText(article.getContent()));
        });
        List<ArticleCardVo> cardVo = ArticleMapping.INSTANCE.toCardVo(collect);
        return Result.success(cardVo);
    }

    @ApiOperation("获取用户创建的帖子列表（作者页面数据）")
    @GetMapping("{userId}/posts")
    public Result<List<UserPostVo>> getUserPosts(@PathVariable Long userId) {
        List<UserPostVo> voList = postsService.getUserPosts(userId);
        return voList != null ? Result.success(voList) : Result.error();
    }

    @ApiOperation("获取用户收藏的帖子列表（作者页面数据）")
    @GetMapping("{userId}/favorites")
    public Result<List<UserPostVo>> getUserFavorites(@PathVariable Long userId) {
        List<UserPostVo> voList = postsService.getUserFavorites(userId);
        return voList != null ? Result.success(voList) : Result.error();
    }
//    /**
// * 更新文章评论区状态
// * @param {number} articleId 文章ID
// * @param {number} status 状态：0-关闭，1-开启
// * @returns {Promise}
// */
//export const updateArticleCommentStatus = (articleId, status) => {
//    return request.put(`/article/${articleId}/comment-status`, { isOpenComment: status });
//};

    @ApiOperation("更新文章评论区状态")
    @PutMapping("{articleId}/comment-status")
    @RequiresPermission(api = {"posts:{id}:comment-status:put"})
    public Result<Void> updateArticleCommentStatus(@PathVariable Long articleId, @RequestBody IdStatusParam param) {
        return postsService.updateArticleCommentStatus(articleId,param) ? Result.success() : Result.error();
    }

}

package com.example.communityserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.request.AddArticleDto;
import com.example.communityserver.entity.request.GetArticleListDto;
import com.example.communityserver.entity.request.SearchParam;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import com.example.communityserver.entity.response.ArticleListVo;
import com.example.communityserver.entity.response.EditorArticlesVo;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.ITagService;
import com.example.communityserver.utils.web.Result;
import com.example.communityserver.utils.web.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 帖子 文章 article
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-23
 **/

@Api(tags = "帖子内容")
@RestController
@RequestMapping("/posts")
public class ArticleController {
    @Autowired
    private IArticleService postsService;

    @Autowired
    private ITagService tagService;

    // TODO: 2025/6/28 游客能够直接使用查看文章功能 
    // TODO: 2025/6/28 权限使用

    @ApiOperation("搜索文章")
    @GetMapping
    public TableDataInfo fetchPosts(SearchParam param) {
        Page<ArticleCardVo> page = postsService.getPostsCardVoList(param);
        TableDataInfo tableDataInfo = new TableDataInfo(page.getRecords(), (int) page.getTotal());
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("成功");
        return tableDataInfo;
    }

    @ApiOperation("文章详情(打开一篇文章)")
    @GetMapping("/{id}")
    public Result<ArticleDtlVo> fetchPostsDetail(@PathVariable Long id) {
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

    @ApiOperation("添加文章")
    @PostMapping("/addArticle")
    public Result<Void> addArticle(@RequestBody AddArticleDto dto) {
        return postsService.addArticle(dto) ? Result.success() : Result.error("失败");
    }

    @ApiOperation("更新文章")
    @PutMapping("/updateArticleDtl/{id}")
    public Result<Void> updateArticleDtl(@PathVariable Long id, @RequestBody @Valid AddArticleDto updateDto) {
        Article article = new Article();
        article.setIsDrafts(updateDto.getStatus());
        article.setArticleId(id);
        article.setFileId(updateDto.getFileId());
        article.setContent(updateDto.getContent());
        article.setTitle(updateDto.getTitle());
        return postsService.updateById(article) ? Result.success() : Result.error("更新失败，稍后重试");
    }

    @ApiOperation("获取文章列表")
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
    public Result<List<Tag>> getPostTags(@PathVariable Long postId) {
        List<Tag> tags = tagService.getPostTags(postId);
        return tags != null ? Result.success(tags) : Result.error();
    }

}

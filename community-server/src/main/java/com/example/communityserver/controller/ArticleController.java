package com.example.communityserver.controller;

import com.example.communityserver.entity.dto.AddArticleDto;
import com.example.communityserver.entity.dto.GetArticleListDto;
import com.example.communityserver.entity.dto.SearchParam;
import com.example.communityserver.entity.po.Article;
import com.example.communityserver.entity.vo.ArticleCardVo;
import com.example.communityserver.entity.vo.ArticleDtlVo;
import com.example.communityserver.entity.vo.ArticleListVo;
import com.example.communityserver.entity.vo.EditorArticlesVo;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.Result;
import com.example.communityserver.utils.TableDataInfo;
import com.github.pagehelper.PageHelper;
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

    @ApiOperation("搜索文章")
    @GetMapping
    public TableDataInfo fetchPosts(SearchParam param) {
        TableDataInfo tableDataInfo = new TableDataInfo();
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<ArticleCardVo> voList = postsService.getPostsCardVoList(param.getTitle());
        List<ArticleCardVo> list = postsService.getPostsCardVoList(param.getTitle());
        System.out.println(voList);
        tableDataInfo.setCode(200);
        tableDataInfo.setTotal(list.size());
        tableDataInfo.setRows(voList);
        tableDataInfo.setMsg("成功");
        return tableDataInfo;
    }

    @ApiOperation("文章详情(打开一篇文章)")
    @GetMapping("/{id}")
    public Result fetchPostsDetail(@PathVariable Long id) {
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
    public Result addArticle(@RequestBody AddArticleDto dto) {
        return postsService.addArticle(dto) ? Result.success("成功") : Result.error("失败");
    }

    @ApiOperation("更新文章")
    @PutMapping("/updateArticleDtl/{id}")
    public Result updateArticleDtl(@PathVariable Long id, @RequestBody @Valid AddArticleDto updateDto) {
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
        TableDataInfo tableDataInfo = new TableDataInfo();
        PageHelper.startPage(dto.getPage(), dto.getSize());
        List<ArticleListVo> listVo = postsService.getArticleList(dto.getTitle(), dto.getStatus(), dto.getSortField(), dto.getIsAsc());
        tableDataInfo.setRows(listVo);
        List<ArticleListVo> listVos = postsService.getArticleList(dto.getTitle(), dto.getStatus(), dto.getSortField(), dto.getIsAsc());
        tableDataInfo.setTotal(listVos.size());
        tableDataInfo.setMsg("成功");
        tableDataInfo.setCode(200);
        return tableDataInfo;
    }

    @ApiOperation("删除文章")
    @DeleteMapping("/del/{id}")
    public Result deleteArticle(@PathVariable Long id) {
        return postsService.delById(id) ? Result.success() : Result.error("失败");
    }

}

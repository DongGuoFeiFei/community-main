package com.example.communityserver.controller;

import com.example.communityserver.entity.dto.AddArticleDto;
import com.example.communityserver.entity.dto.SearchParam;
import com.example.communityserver.entity.vo.ArticleCardVo;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.utils.Result;
import com.example.communityserver.utils.TableDataInfo;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("文章详情")
    @GetMapping("/{id}")
    public TableDataInfo fetchPostsDetail(@PathVariable Long id) {
        TableDataInfo tableDataInfo = new TableDataInfo();
        List<ArticleCardVo> voList = postsService.getPostsCardVoById(id);
        List<ArticleCardVo> list = postsService.getPostsCardVoById(id);
        tableDataInfo.setCode(200);
        tableDataInfo.setTotal(list.size());
        tableDataInfo.setRows(voList);
        tableDataInfo.setMsg("成功");
        return tableDataInfo;
    }

    @ApiOperation("添加文章")
    @PostMapping("/addArticle")
    public Result addArticle(@RequestBody AddArticleDto dto) {
        return postsService.addArticle(dto) ? Result.success("成功") : Result.error("失败");
    }

}

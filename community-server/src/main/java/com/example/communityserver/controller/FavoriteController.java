package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.dto.AddFavoriteArticle;
import com.example.communityserver.entity.dto.AddName;
import com.example.communityserver.entity.po.FavoriteArticle;
import com.example.communityserver.entity.vo.FavArticleVo;
import com.example.communityserver.entity.vo.FoldersVo;
import com.example.communityserver.service.IFavoriteArticleService;
import com.example.communityserver.service.IFavoriteFolderService;
import com.example.communityserver.service.IFavoriteService;
import com.example.communityserver.utils.Result;
import com.example.communityserver.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Api(tags = "收藏管理")
@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private IFavoriteService favoriteService;

    @Autowired
    private IFavoriteFolderService favoriteFolderService;

    @Autowired
    private IFavoriteArticleService favoriteArticleService;

    @ApiOperation("获取收藏夹列表")
    @GetMapping("/folders")
    public Result<List<FoldersVo>> getFavoriteFolder() {
        // 获取文件夹列表
        List<FoldersVo> voList = favoriteFolderService.getFavoriteFolder();

        return voList != null ? Result.success(voList) : Result.error();
    }

    @ApiOperation("新建收藏夹")
    @PostMapping("/folders")
    public Result<FoldersVo> addFolder(@RequestBody AddName param) {
        FoldersVo foldersVo = favoriteFolderService.addFolder(param.getName());
        return foldersVo != null ? Result.success(foldersVo) : Result.error();
    }

    @ApiOperation("收藏文章")
    @PostMapping
    public Result<FavArticleVo> addFavArticle(@RequestBody AddFavoriteArticle param) {
        FavArticleVo favArticleVo = favoriteArticleService.addFavArticle(param);
        return favArticleVo != null ? Result.success(favArticleVo) : Result.error();

    }

    @ApiOperation("取消文章收藏")
    @DeleteMapping("/{articleId}")
    public Result delFavArticle(@PathVariable Long articleId) {
        LambdaQueryWrapper<FavoriteArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(articleId != null, FavoriteArticle::getArticleId, articleId)
                .eq(SecurityUtils.getLoginUserId() != null, FavoriteArticle::getUserId, SecurityUtils.getLoginUserId());
        boolean remove = favoriteArticleService.remove(queryWrapper);
        return remove ? Result.success() : Result.error();

    }

    // todo 删除收藏夹 …………

}

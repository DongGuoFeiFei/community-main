package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.request.*;
import com.example.communityserver.entity.response.FavArticleVo;
import com.example.communityserver.entity.response.FolderVo;
import com.example.communityserver.entity.response.MoveFavoriteVo;
import com.example.communityserver.entity.response.UserFavoListVo;
import com.example.communityserver.service.IFavoriteFolderService;
import com.example.communityserver.service.IUserFavoriteService;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private IFavoriteFolderService favoriteFolderService;

    @Autowired
    private IUserFavoriteService userFavoriteService;

    @ApiOperation("获取收藏夹列表")
    @GetMapping("/folders")
    public Result<List<FolderVo>> getFavoriteFolder() {
        // 获取文件夹列表
        List<FolderVo> voList = favoriteFolderService.getFavoriteFolder();
        return voList != null ? Result.success(voList) : Result.error();
    }

    @ApiOperation("新建收藏夹")
    @PostMapping("/folders")
    public Result<FolderVo> addFolder(@RequestBody AddFolderNameParam param) {
        FolderVo folderVo = favoriteFolderService.addFolder(param.getName());
        return folderVo != null ? Result.success(folderVo) : Result.error();
    }

    @ApiOperation("收藏文章")
    @PostMapping
    public Result<FavArticleVo> addFavArticle(@RequestBody @Valid AddFavoriteArticle param) {
        FavArticleVo favArticleVo = userFavoriteService.addFavArticle(param);
        return favArticleVo != null ? Result.success(favArticleVo) : Result.error();
    }

    @ApiOperation("取消文章收藏")
    @DeleteMapping("/{articleId}")
    public Result<Void> delFavArticle(@PathVariable Long articleId) {
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(articleId != null, UserFavorite::getTargetId, articleId)
                .eq(SecurityUtils.getLoginUserId() != null, UserFavorite::getUserId, SecurityUtils.getLoginUserId());
        boolean remove = userFavoriteService.remove(queryWrapper);
        return remove ? Result.success() : Result.error();

    }

    // TODO: 2025/6/23 目前一个文章一个人只能收藏一个，修改为可以重复收藏在不同的收藏夹中 
    @ApiOperation("修改收藏夹名称")
    @PutMapping("/folders/{folderId}")
    public Result<FolderVo> modifyFolderName(@PathVariable Long folderId, @RequestBody ModifyFolderNameParam param) {
        FolderVo folderVo = favoriteFolderService.modifyFolderName(folderId, param);
        return folderVo != null ? Result.success(200, "收藏夹名称更新成功", folderVo) : Result.error("收藏夹名称更新失败");
    }

    @ApiOperation("删除收藏夹")
    @DeleteMapping("/folders/{folderId}")
    public Result<Void> delFolder(@PathVariable Long folderId) {
        // 删除--收藏夹下的收藏一并删除
        return favoriteFolderService.delFolder(folderId) ? Result.success(200, "收藏夹删除成功", null) : Result.error("收藏夹删除失败");
    }

    @ApiOperation("获取收藏的文章列表")
    @GetMapping("/articles")
    public Result<Result.PageData<UserFavoListVo>> getUserFavoListVo(GetUserFavoListParam param) {
        // 使用mybatisPlus自带分页进行分页
        Page<UserFavoListVo> page = userFavoriteService.getUserFavoListVo(param);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("移动收藏文章")
    @PutMapping("/move")
    public Result<MoveFavoriteVo> moveFavoriteVoResult(@RequestBody MoveFavoriteDto dto) {
        MoveFavoriteVo vo = userFavoriteService.moveFavoriteVoResult(dto);
        return vo != null ? Result.success(vo) : Result.error();
    }

    // TODO: 2025/6/23 后续添加   复制文章收藏到某收藏夹中
    // TODO: 2025/6/25 目前不能同时收藏多个文章，前端一次性创建多次收藏夹时，有着连同效果（同时选中，同时取消，连体了） 
    // TODO: 2025/6/25 后续再文章中取消收藏时，要先打开收藏夹弹窗再取消

    // TODO: 2025/6/27 将user_favorite中的article_id改成了target_id
}

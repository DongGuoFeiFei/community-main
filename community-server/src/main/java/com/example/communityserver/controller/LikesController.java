package com.example.communityserver.controller;

import com.example.communityserver.service.ILikesService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-24
 **/

@Api(tags = "点赞管理")
@RestController
@RequestMapping("/like")
public class LikesController {
    @Autowired
    private ILikesService likesService;

    @ApiOperation("添加或改变当前文章的喜欢")
    @PutMapping("/addLike/{id}")
    public Result<Void> addLike(@PathVariable Long id) {
// TODO: 2025/7/7 改为互动表
        return likesService.addLike(id) ? Result.success() : Result.error();
    }

}

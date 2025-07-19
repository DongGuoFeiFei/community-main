package com.example.communityserver.controller;

import com.example.communityserver.entity.response.FollowVo;
import com.example.communityserver.service.IFollowService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-15
 **/

@Api(tags = "关注")
@RestController
@RequestMapping("follow")
public class FollowController {
    @Autowired
    private IFollowService followService;

    @ApiOperation("添加关注")
    @PostMapping("addFollowAuthor/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> followAuthor(@PathVariable Long id) {
        Boolean is = followService.followAuthor(id);
        return is ? Result.success() : Result.error();
    }

    @ApiOperation("取消关注")
    @DeleteMapping("delFollowAuthor/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> delFollowAuthor(@PathVariable Long id) {
        Boolean is = followService.delFollowAuthor(id);
        return is ? Result.success() : Result.error();
    }

    @ApiOperation("是否关注")
    @GetMapping("isFollowing/{id}")
    public Result<Boolean> isFollowing(@PathVariable Long id) {
        Boolean is = followService.isFollowing(id);
        return Result.success(is);
    }

    @ApiOperation("获取关注列表")
    @GetMapping("{userId}/following")
    public Result<List<FollowVo>> getFollowingList(@PathVariable Long userId) {
        List<FollowVo> followVos = followService.getFollowingList(userId);
        return Result.success(followVos);
    }

    @ApiOperation("获取粉丝列表")
    @GetMapping("{userId}/followers")
    public Result<List<FollowVo>> getFollowerList(@PathVariable Long userId) {
        List<FollowVo> followVos = followService.getFollowerList(userId);
        return Result.success(followVos);
    }

    @ApiOperation("移除粉丝")
    @DeleteMapping("fan/{fanId}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> removeFan(@PathVariable Long fanId) {
        Boolean is = followService.removeFan(fanId);
        return is ? Result.success() : Result.error();
    }
}

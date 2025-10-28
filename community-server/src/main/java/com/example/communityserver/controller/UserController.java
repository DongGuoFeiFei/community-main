package com.example.communityserver.controller;


import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.IdIdsParam;
import com.example.communityserver.entity.request.UpdateUserInfo;
import com.example.communityserver.entity.response.AuthorInfoVo;
import com.example.communityserver.entity.response.UserCountStats;
import com.example.communityserver.entity.response.UserDelVo;
import com.example.communityserver.core.security.aop.Logical;
import com.example.communityserver.core.security.aop.RequiresPermission;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "用户信息")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFileEntityService fileEntityService;

    @PostMapping("/updateUserProfile")
    @ApiOperation("修改用户信息")
    @RequiresPermission(api = {"user:updateUserProfile:post"} ,role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> updateUserProfile(@RequestBody UpdateUserInfo info) {
        User user = new User();
        user.setUserId(SecurityUtils.getLoginUserId());
        // TODO: 2025/5/10 密码验证，邮箱确认
        BeanUtils.copyProperties(info, user);
        return userService.updateById(user) ? Result.success() : Result.error("error");
    }

    @PostMapping("/updateUserCoverId")
    @ApiOperation("更换用户头像")
    @RequiresPermission(api = {"user:updateUserCoverId:post"} ,role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> updateUserCoverId(@RequestBody Long fileId) {
        Long loginUserId = SecurityUtils.getLoginUserId();
        User user = userService.getById(loginUserId);
        if (user.getFileId() > 10) {
            fileEntityService.delFileById(user.getFileId());
        }
        FileEntity fileEntity = fileEntityService.getById(fileId);
        user.setUserId(loginUserId);
        user.setFileId(fileId);
        user.setAvatar(fileEntity.getAccessUrl());
        return userService.updateById(user) ? Result.success() : Result.error("更换失败。");
    }

    @ApiOperation("获取作者详情")
    @GetMapping("/{articleId}/info")
    public Result<AuthorInfoVo> getAuthorInfoVo(@PathVariable Long articleId) {
        AuthorInfoVo vo = userService.getAuthorInfoVo(articleId);
        return vo != null ? Result.success(vo) : Result.error();
    }

    @ApiOperation("获取用户的相关信息数量")
    @GetMapping("/{userId}/stats")
    public Result<UserCountStats> getUserStats(@PathVariable Long userId) {
        UserCountStats stats = userService.getUserStats(userId);
        return stats != null ? Result.success(stats) : Result.error();
    }

    @ApiOperation("获取作者页面详情信息")
    @GetMapping("/profile/{userId}")
    public Result<UserDelVo> getUserProfile(@PathVariable Long userId) {
        UserDelVo vo = userService.getUserProfile(userId);
        return vo != null ? Result.success(vo) : Result.error();
    }


    @ApiOperation("更新用户角色")
    @PutMapping("roles")
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermission(api = {"user:roles:put"},role = {"super_admin"}, logical = Logical.OR)
    public Result<Void> updateUserRoles(@RequestBody IdIdsParam param) {
        Integer is = userService.updateUserRoles(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }


    // TODO: 2025/6/28 用户信息方面需要修改

}

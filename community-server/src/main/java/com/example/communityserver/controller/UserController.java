package com.example.communityserver.controller;


import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.UpdateUserInfo;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Void> updateUserProfile(@RequestBody UpdateUserInfo info) {
        User user = new User();
        user.setUserId(SecurityUtils.getLoginUserId());
        // TODO: 2025/5/10 密码验证，邮箱确认
        BeanUtils.copyProperties(info, user);
        return userService.updateById(user) ? Result.success() : Result.error("error");
    }

    @PostMapping("/updateUserCoverId")
    @ApiOperation("更换用户头像")
    public Result<Void> updateUserCoverId(@RequestBody Long fileId) {
        Long loginUserId = SecurityUtils.getLoginUserId();
        User user = userService.getById(loginUserId);
        if (user.getFileId() > 10) {
            fileEntityService.delFileById(user.getFileId());
        }
        user.setUserId(loginUserId);
        user.setFileId(fileId);
        return userService.updateById(user) ? Result.success() : Result.error("更换失败。");
    }

    // TODO: 2025/6/28 用户信息方面需要修改 

}

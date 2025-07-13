package com.example.communityserver.adminController;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.constants.SecurityConstants;
import com.example.communityserver.entity.enums.MessageCodeEnum;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.IdStatusParam;
import com.example.communityserver.entity.request.IdsListParam;
import com.example.communityserver.entity.request.ModifyUserParam;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.UserListVo;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-04
 **/

@Api(tags = "管理员用户管理")
@RestController
@RequestMapping("admin/users")
public class AdminUserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IEmailService emailService;

    @ApiOperation("获取用户列表")
    @GetMapping()
    public Result<Result.PageData<UserListVo>> getUsers(UserSearchParam param) {
        IPage<UserListVo> page = userService.getUsers(param);
        return page != null ? Result.pageSuccess(page.getTotal(), page.getRecords()) : Result.error();
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId, userId).set(User::getIsDel, 1);
        return userService.update(updateWrapper) ? Result.success() : Result.error();
    }

    @ApiOperation("批量删除用户")
    @PostMapping("/batch-delete")
    public Result<Void> batchDeleteUser(@RequestBody IdsListParam param) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(User::getUserId, param.getIds()).set(User::getIsDel, 1);
        return userService.update(updateWrapper) ? Result.success() : Result.error();
    }

    @ApiOperation("改变激活用户状态")
    @PostMapping("/active-change")
    public Result<Void> activeChange(@RequestBody IdStatusParam param) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId, param.getId()).set(User::getIsActive, param.getStatus());
        return userService.update(updateWrapper) ? Result.success() : Result.error();
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/{userId}")
    public Result<Void> activeChange(@PathVariable Long userId, @RequestBody ModifyUserParam param) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(User::getUserId, userId)  // 确保只能更新自己的信息
                .set(param.getUsername() != null, User::getUsername, param.getUsername())
                .set(param.getNickname() != null, User::getNickname, param.getNickname())
                .set(param.getEmail() != null, User::getEmail, param.getEmail())
                .set(param.getPhone() != null, User::getPhone, param.getPhone())
                .set(param.getIsActive() != null, User::getIsActive, param.getIsActive());
        // 执行更新并检查影响行数
        boolean success = userService.update(updateWrapper);
        return success ? Result.success() : Result.error();
    }

    @ApiOperation("添加用户")
    @PutMapping("/addUser")
    public Result<Void> addUser(@RequestBody ModifyUserParam param) {
        User user = new User();
        MessageCodeEnum existUser = userService.isExistUser(null, param.getUsername(), null);
        if (existUser != MessageCodeEnum.USER_NOT_EXIST) {
            return Result.error(existUser.getValue());
        }
        user.setNickname(param.getNickname());
        user.setUsername(param.getUsername());
        user.setIsActive(param.getIsActive());
        user.setPassword(SecurityUtils.encryptPassword(SecurityConstants.ORIGINAL_PASSWORD));
        user.setAvatar(SecurityConstants.ORIGINAL_AVATAR);
        return Result.success();
    }


}

package com.example.communityserver.controller;


import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.SecurityConstants;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.enums.ValidateCodeTypeEnum;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.request.GetValidateCodeDto;
import com.example.communityserver.entity.request.LoginRequest;
import com.example.communityserver.entity.request.RegisterDto;
import com.example.communityserver.entity.response.LoginResponse;
import com.example.communityserver.core.security.util.JWTUtil;
import com.example.communityserver.core.security.util.PermissionExpressionUtil;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.ILoginLogService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.common.CaptchaUtil;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.web.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "登录等验证")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFileEntityService fileEntityService;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IUserService userService;

    // TODO: 2025/5/21 @PreAuthorize("@vip.myAuthority('superAdmin')")权限划分失败
    @ApiOperation("生成验证码图片")
    @GetMapping("/captcha")
    public Result<CaptchaUtil.Captcha> getCaptcha() {
        // 生成验证码
        CaptchaUtil.Captcha captcha = CaptchaUtil.generate();

        // 生成验证码key
        String captchaKey = UUID.randomUUID().toString();

        // 存储验证码到Redis，有效期5分钟
        redisUtil.setCacheObject(CacheKeyConstants.CAPTCHA_CODE + captchaKey, captcha.getCode(), 5, TimeUnit.MINUTES);

        // 返回验证码图片和key
        return Result.success(new CaptchaUtil.Captcha(captchaKey, captcha.getImage()));
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        LoginRequest loginRequest = new LoginRequest(requestBody.get("username"), requestBody.get("password"), requestBody.get("captchaCode"), requestBody.get("captchaKey"));
        LoginResponse loginResponse = new LoginResponse();

        // 验证验证码是否正确
        String redisKey = CacheKeyConstants.CAPTCHA_CODE + loginRequest.getCaptchaKey();
        String correctCode = redisUtil.getCacheObject(redisKey);
        if (correctCode == null) {
            return Result.error("验证码已过期");
        }
        if (!correctCode.equalsIgnoreCase(loginRequest.getCaptchaCode())) { // 忽视大小写
            return Result.error("验证码错误");
        }
        // 验证码验证通过后删除
        redisUtil.deleteObject(redisKey);

        // 此次获取的token
        String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 此时，token还未传入security中
        loginResponse.setToken(token);
        Long userId = JWTUtil.getUserId(loginResponse.getToken());
        // 根据userId从redis中拿到loginUser数据
        LoginUser loginUser = redisUtil.getCacheObject(CacheKeyConstants.LOGIN_USER_ID + userId);
        loginUser.getUser().setPassword("");
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn((int) SecurityConstants.TOKEN_EXPIRATION);

        loginResponse.setUserInfo(loginUser.getUser());

        // TODO: 2025/7/6 头像取消连接文件表
        FileEntity fileEntity = fileEntityService.getById(loginUser.getUser().getFileId());
        loginResponse.setAvatarUrl(fileEntity.getAccessUrl());

        // 添加登录日志
        String lastTime = loginLogService.addLoginLog(request, userId);
        loginResponse.getUserInfo().setLastLogin(lastTime);

        return Result.success(200, "登录成功", loginResponse);
    }

    @PostMapping("/admin/login")
    public Result<LoginResponse> loginAdmin(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        LoginRequest loginRequest = new LoginRequest(requestBody.get("username"), requestBody.get("password"), requestBody.get("captchaCode"), requestBody.get("captchaKey"));
        LoginResponse loginResponse = new LoginResponse();

        // 验证验证码是否正确
        String redisKey = CacheKeyConstants.CAPTCHA_CODE + loginRequest.getCaptchaKey();
        String correctCode = redisUtil.getCacheObject(redisKey);
        if (correctCode == null) {
            return Result.error("验证码已过期");
        }
        if (!correctCode.equalsIgnoreCase(loginRequest.getCaptchaCode())) { // 忽视大小写
            return Result.error("验证码错误");
        }
        // 验证码验证通过后删除
        redisUtil.deleteObject(redisKey);

        // 此次获取的token
        String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 此时，token还未传入security中
        loginResponse.setToken(token);
        Long userId = JWTUtil.getUserId(loginResponse.getToken());
        // 根据userId从redis中拿到loginUser数据
        LoginUser loginUser = redisUtil.getCacheObject(CacheKeyConstants.LOGIN_USER_ID + userId);

        // 验证是否是拥有登录管理员的权限
        List<String> roleList = new ArrayList<>();
        roleList.add(SecurityConstants.SYSTEM_ADMIN);
        roleList.add(SecurityConstants.SUPER_ADMIN);
        roleList.add("view_admin");
        Boolean isPerm = PermissionExpressionUtil.hasAnyRolePerm(loginUser.getRoles(), roleList);
        if (!isPerm) {
            return Result.error("没有登录权限");
        }
        loginUser.getUser().setPassword("");
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn((int) SecurityConstants.TOKEN_EXPIRATION);

        loginResponse.setUserInfo(loginUser.getUser());

        // TODO: 2025/7/6 头像取消连接文件表
        FileEntity fileEntity = fileEntityService.getById(loginUser.getUser().getFileId());
        loginResponse.setAvatarUrl(fileEntity.getAccessUrl());

        // 添加登录日志
        String lastTime = loginLogService.addLoginLog(request, userId);
        loginResponse.getUserInfo().setLastLogin(lastTime);

        return Result.success(200, "登录成功", loginResponse);
    }

    @ApiOperation("获取新的token")
    @PostMapping("/refreshToken")
    public Result<String> refreshToken() {
        return Result.success(JWTUtil.createToken(SecurityUtils.getLoginUserId()));
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        redisUtil.deleteObject(CacheKeyConstants.LOGIN_USER_ID + SecurityUtils.getLoginUserId());
        return Result.success();
    }

    @ApiOperation("发送注册验证码到注册邮箱")
    @PostMapping("/registerCode")
    public Result<Void> registerCode(@RequestBody GetValidateCodeDto dto) {
        if (!StringUtil.isValidEmail(dto.getEmail())) {
            return Result.error("邮箱不符合要求");
        }
        if (!Objects.equals(dto.getType(), ValidateCodeTypeEnum.REGISTER.getValue())) {
            return Result.error("验证码类型不符合要求");
        }
        if (StringUtil.isBlank(dto.getUsername())) {
            return Result.error("用户名不符合要求");
        }

        // 验证库中邮箱和用户名是否重复
        ResponseCodeEnum codeEnum = userService.isExistUser(dto.getEmail(), dto.getUsername(), null);
        if (codeEnum != ResponseCodeEnum.USER_NOT_EXIST) {
            return Result.error(codeEnum.getValue());
        }
        emailService.sendVerificationCode(dto.getEmail(), dto.getNickname());
        return Result.success();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> register(@RequestBody RegisterDto dto) {
        if (!StringUtil.isValidEmail(dto.getEmail())) {
            return Result.error("邮箱不符合要求");
        }
        if (StringUtil.isBlank(dto.getUsername())) {
            return Result.error("用户名不符合要求");
        }
        if (!emailService.verifyCode(dto.getEmail(), dto.getCode())) {
            return Result.error("验证码错误");
        }
        ResponseCodeEnum codeEnum = userService.register(dto);
        if (codeEnum == ResponseCodeEnum.USERNAME_EXIST) {
            return Result.error(codeEnum.getValue());
        }
        // todo 注册失败
        emailService.sendWelcomeEmail(dto.getEmail(), dto.getNickname());

        // TODO: 2025/7/15 完善数据库相关表格数据的插入 ，用户详情表
        return Result.success();
    }


}

package com.example.communityserver.controller;


import com.example.communityserver.entity.dto.LoginRequest;
import com.example.communityserver.entity.po.FileEntity;
import com.example.communityserver.entity.po.LoginUser;
import com.example.communityserver.entity.vo.LoginResponse;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.ILoginLogService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.JWTUtil;
import com.example.communityserver.utils.RedisUtil;
import com.example.communityserver.utils.Result;
import com.example.communityserver.utils.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@Api(tags = "登录等验证")
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private IUserService service;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IFileEntityService fileEntityService;

    @Autowired
    private ILoginLogService loginLogService;

    @Autowired
    private IEmailService emailService;

    // TODO: 2025/5/21 @PreAuthorize("@vip.myAuthority('superAdmin')")权限划分失败
    // TODO: 2025/6/2 后续需要处理，如果用户没有登录就默认是游客登录
    // TODO: 2025/6/23 动态生成一个验证码，验证真人验证码 
    /*
    隐私合规：确保符合GDPR等隐私法规，在隐私政策中说明数据收集方式
    数据清理：设置合理的过期时间，避免存储过多无用数据
    用户登录后合并数据：当用户登录时，将匿名期间的浏览记录合并到用户账户
    防刷机制：后端接口应添加适当的限流措施，防止恶意刷量
    */
    /*
    注册新用户时，同步新建一个文章默认收藏夹
     */
    @PostMapping("/login")
    private Result login(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        LoginRequest loginRequest = new LoginRequest(requestBody.get("username"), requestBody.get("password"));
        LoginResponse loginResponse = new LoginResponse();
        String token = service.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 此时，token还未传入security中
        loginResponse.setToken(token);
        Long userId = JWTUtil.getUserId(loginResponse.getToken());
        // 根据userId从redis中拿到loginUser数据
        LoginUser loginUser = redisUtil.getCacheObject("user:" + userId);
        loginUser.getUser().setPassword("");
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn(259200);

        loginResponse.setUserInfo(loginUser.getUser());
        FileEntity fileEntity = fileEntityService.getById(loginUser.getUser().getFileId());
        loginResponse.setAvatarUrl(fileEntity.getAccessUrl());

        // 添加登录日志
        String lastTime = loginLogService.addLoginLog(request, userId);
        loginResponse.getUserInfo().setLastLogin(lastTime);

        return Result.success(200, "登录成功", loginResponse);
    }

    @ApiOperation("获取新的token")
    @PostMapping("/newToken")
    public Result newToken() {
        return Result.success(JWTUtil.createToken(SecurityUtils.getLoginUserId()));
    }

//    @ApiOperation("注册")
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        // 1. 校验邮箱格式
//        if (!request.getEmail().matches(Constants.REGEX_EMAIL)) {
//            throw new IllegalArgumentException("邮箱格式不正确");
//        }
//
//        // 2. 发送验证码（异步处理，避免阻塞）
//        CompletableFuture.runAsync(() -> {
//            try {
//                emailService.sendVerificationCode(request.getEmail(), request.getUsername());
//            } catch (MessagingException e) {
//                log.error("邮件发送失败: {}", e.getMessage());
//            }
//        });
//
//        return ResponseEntity.ok().body("验证码已发送至您的邮箱");
//    }
//
//    @ApiOperation("验证码校验接口")
//    @PostMapping("/verify-code")
//    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {
//        // 从Redis中获取验证码并校验
//        // String storedCode = redisTemplate.opsForValue().get("verify:" + email);
//        // if (storedCode == null || !storedCode.equals(code)) {
//        //     throw new IllegalArgumentException("验证码无效或已过期");
//        // }
//        // 校验通过后发送欢迎邮件
//        emailService.sendWelcomeEmail(email, "用户昵称");
//        return ResponseEntity.ok().body("注册成功");
//    }
}
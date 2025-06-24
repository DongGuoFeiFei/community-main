package com.example.communityserver.controller;


import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.enums.MessageCodeEnum;
import com.example.communityserver.entity.enums.ValidateCodeTypeEnum;
import com.example.communityserver.entity.model.FileEntity;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.request.GetValidateCodeDto;
import com.example.communityserver.entity.request.LoginRequest;
import com.example.communityserver.entity.request.RegisterDto;
import com.example.communityserver.entity.response.LoginResponse;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.ILoginLogService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.common.CaptchaUtil;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.JWTUtil;
import com.example.communityserver.utils.security.SecurityUtils;
import com.example.communityserver.utils.web.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    // TODO: 2025/5/21 @PreAuthorize("@vip.myAuthority('superAdmin')")权限划分失败
    // TODO: 2025/6/2 后续需要处理，如果用户没有登录就默认是游客登录
    // TODO: 2025/6/24 限制登录次数，登录5次限制登录
    // TODO: 2025/6/24 退出登录，销毁token
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
    private Result<LoginResponse> login(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        LoginRequest loginRequest = new LoginRequest(
                requestBody.get("username"),
                requestBody.get("password"),
                requestBody.get("captchaCode"),
                requestBody.get("captchaKey")
        );
        LoginResponse loginResponse = new LoginResponse();

        // 验证验证码是否正确
        String redisKey = CacheKeyConstants.CAPTCHA_CODE + loginRequest.getCaptchaKey();
        String correctCode = redisTemplate.opsForValue().get(redisKey);
        if (correctCode == null) {
            return Result.error("验证码已过期");
        }
        if (!correctCode.equalsIgnoreCase(loginRequest.getCaptchaCode())) { // 忽视大小写
            return Result.error("验证码错误");
        }
        // 验证码验证通过后删除
        redisTemplate.delete(redisKey);


        // 此次获取的token
        String token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 此时，token还未传入security中
        loginResponse.setToken(token);
        Long userId = JWTUtil.getUserId(loginResponse.getToken());
        // 根据userId从redis中拿到loginUser数据
        LoginUser loginUser = redisUtil.getCacheObject("user:" + userId);
        loginUser.getUser().setPassword("");
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn((int) (3 * SystemConstants.ONE_DAY_MILLIS / 1000));

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
    public Result<String> refreshToken() {
        return Result.success(JWTUtil.createToken(SecurityUtils.getLoginUserId()));
    }

    @ApiOperation("发送验证码")
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
        MessageCodeEnum codeEnum = userService.isExistUser(dto.getEmail(), dto.getUsername());
        if (codeEnum != null) {
            return Result.error(codeEnum.getValue());
        }
        emailService.sendVerificationCode(dto.getEmail(), dto.getNickname());
        return Result.success();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
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
        MessageCodeEnum codeEnum = userService.register(dto);
        if (codeEnum == MessageCodeEnum.USERNAME_EXIST) {
            return Result.error(codeEnum.getValue());
        }
        emailService.sendWelcomeEmail(dto.getEmail(), dto.getNickname());

        return Result.success();
    }

    // 生成验证码图片
    @ApiOperation("生成验证码图片")
    @GetMapping("/captcha")
    public Result<CaptchaUtil.Captcha> getCaptcha() {
        // 生成验证码
        CaptchaUtil.Captcha captcha = CaptchaUtil.generate();

        // 生成验证码key
        String captchaKey = UUID.randomUUID().toString();

        // 存储验证码到Redis，有效期5分钟
        redisTemplate.opsForValue().set(
                CacheKeyConstants.CAPTCHA_CODE + captchaKey,
                captcha.getCode(),
                5,
                TimeUnit.MINUTES
        );

        // 返回验证码图片和key
        return Result.success(new CaptchaUtil.Captcha(captchaKey, captcha.getImage()));
    }


}
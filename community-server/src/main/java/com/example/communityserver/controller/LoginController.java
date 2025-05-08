package com.example.communityserver.controller;


import com.example.communityserver.entity.dto.LoginRequest;
import com.example.communityserver.entity.po.FileEntity;
import com.example.communityserver.entity.po.LoginUser;
import com.example.communityserver.entity.vo.LoginResponse;
import com.example.communityserver.service.IFileEntityService;
import com.example.communityserver.service.ILoginLogService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.JWTUtil;
import com.example.communityserver.utils.RedisUtil;
import com.example.communityserver.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
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

    @PostMapping("/login")
    private Result login(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody = objectMapper.readValue(
                request.getInputStream(),
                Map.class
        );
        LoginRequest loginRequest = new LoginRequest(requestBody.get("username"), requestBody.get("password"));
        System.out.println(loginRequest);
        LoginResponse loginResponse = new LoginResponse();
        String token = service.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 更新登录的时间。
        if (token != null || !token.equals("")) {

        }
        // 此时，token还未传入security中
        loginResponse.setToken(token);
        Long userId = JWTUtil.getUserId(loginResponse.getToken());
        // 根据userId从redis中拿到loginUser数据
        LoginUser loginUser = redisUtil.getCacheObject("user:" + userId);
        loginUser.getUser().setPassword("");
        loginResponse.setTokenType("Bearer");
        loginResponse.setExpiresIn(30);

        loginResponse.setUserInfo(loginUser.getUser());
        FileEntity fileEntity = fileEntityService.getById(loginUser.getUser().getFileId());
        loginResponse.setAvatarUrl(fileEntity.getAccessUrl());

        // 添加登录日志
        String lastTime = loginLogService.addLoginLog(request, userId);
        loginResponse.getUserInfo().setLastLogin(lastTime);

        return Result.success(200, "登录成功", loginResponse);
    }

}
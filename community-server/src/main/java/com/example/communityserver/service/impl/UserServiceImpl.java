package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.communityserver.entity.po.LoginUser;
import com.example.communityserver.entity.po.User;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.JWTUtil;
import com.example.communityserver.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(String username, String password) {
        // 把账号密码传入security提供的类中封装
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 这里会去调用loadUserByUsername方法，得到LoginUser对象
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // authenticate如果不是null，就认证通过
        if (authenticate == null) {
            throw new RuntimeException("认证没通过");
        }
        // 获取authenticate中的LoginUser对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 把loginUser存入redis中
        redisUtil.setCacheObject("user:" + loginUser.getUser().getUserId(), loginUser);
        return JWTUtil.createToken(loginUser.getUser().getUserId());
    }
// TODO: 2025/4/21 退出登录接口

//    @Override
//    public String logout() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        Long userid = loginUser.getUser().getId();
//        redisCache.deleteObject("login:"+userid);
//        return new ResponseResult(200,"退出成功");
//    }

}

package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.enums.MessageCodeEnum;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.RegisterDto;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.JWTUtil;
import com.example.communityserver.utils.security.SecurityUtils;
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

    @Autowired
    private UserMapper userMapper;

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
        redisUtil.setCacheObject(CacheKeyConstants.LOGIN_USER_ID + loginUser.getUser().getUserId(), loginUser);
        return JWTUtil.createToken(loginUser.getUser().getUserId());
    }


    /**
     * @Description: 返回错误码，不存在，返回null
     * @Param: [email, username]
     * @return: com.example.communityserver.entity.enums.MessageCodeEnum
     * @Author: DongGuo
     */

    @Override
    public MessageCodeEnum isExistUser(String email, String username) {
        // 验证邮箱、用户名、用户名和邮箱
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        User user;
        queryWrapper.eq(email != null, User::getEmail, email);
        user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return MessageCodeEnum.EMAIL_EXIST;
        }
        queryWrapper.eq(username != null, User::getUsername, username);
        user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return MessageCodeEnum.USERNAME_EXIST;
        }
        return null;
    }


    @Override
    public MessageCodeEnum register(RegisterDto dto) {
        MessageCodeEnum codeEnum = isExistUser(dto.getEmail(), dto.getUsername());
        if (codeEnum != null) {
            return codeEnum;
        }
        //注册
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(SecurityUtils.encryptPassword(dto.getPassword()));
        int insert = userMapper.insert(user);
        if (insert > 0) {
            return MessageCodeEnum.REGISTER_SUCCESS;
        } else {
            return MessageCodeEnum.REGISTER_ERROR;
        }
    }


}

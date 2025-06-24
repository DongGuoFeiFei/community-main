package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /**
     * @Description: 自定义获取用户、权限得到loginUser部分
     * @Param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: DongGuo
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<User>();
        qw.eq(User::getUsername, username);
        // 目标user对象
        User user = userMapper.selectOne(qw);

        if (user == null) {
            throw new RuntimeException("用户为空");
        }

        List<String> permission = userMapper.queryPermissionByUserId(user.getUserId());
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setPermissions(permission);
        return loginUser;
    }

}

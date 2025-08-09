package com.example.communityserver.security.core;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.mapper.RoleMapper;
import com.example.communityserver.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

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

        // 2. 查询用户权限
        List<String> permissions = userMapper.queryPermissionByUserId(user.getUserId());

        // 3. 查询用户角色（可选）
        List<String> roles = roleMapper.selectRoleKeysByUserId(user.getUserId());

        // 4. 构建LoginUser对象
        LoginUser loginUser = new LoginUser(user, permissions, roles);

        log.info("用户[{}]登录成功，权限: {}", username, permissions);
        return loginUser;
    }

}

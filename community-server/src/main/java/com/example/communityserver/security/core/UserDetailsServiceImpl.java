package com.example.communityserver.security.core;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.mapper.MenuMapper;
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

    @Autowired
    private MenuMapper menuMapper;

    /**
     * @Description: 自定义获取用户、权限得到loginUser部分
     * @Param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: DongGuo
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 目标user对象
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if (user == null) {
            throw new RuntimeException("用户为空");
        }

        // 2. 查询用户接口权限
        List<String> apis = userMapper.queryPermissionByUserId(user.getUserId());

        // 3. 查询用户角色
        List<String> roles = roleMapper.selectRoleKeysByUserId(user.getUserId());

        // 3. 查询用户菜单权限
        // TODO: 2025/8/17 该方法已被禁用，使用角色获取菜单 
        List<String> menus = menuMapper.selectMenuKeysByUserId(user.getUserId());

        // 4. 构建LoginUser对象
        LoginUser loginUser = new LoginUser(user, apis, roles, menus);

        return loginUser;
    }

}

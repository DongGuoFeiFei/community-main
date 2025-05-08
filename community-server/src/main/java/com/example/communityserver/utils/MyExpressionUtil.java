package com.example.communityserver.utils;


import com.example.communityserver.entity.po.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("vip")
public class MyExpressionUtil {

    // TODO: 2025/4/21 权限与角色、用户与角色  也可以设置实体类，进行管理
    public boolean myAuthority(String permission) {
        // 从上下文拿到authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 从authentication拿到LoginUser对象
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 从LoginUser对象中拿到permissions
        List<String> permissions = loginUser.getPermissions(); // [ k1 ]
        // 做判断
        System.out.println(getClass().getSimpleName()+":"+permissions.contains(permission));
        return permissions.contains(permission);
    }

}

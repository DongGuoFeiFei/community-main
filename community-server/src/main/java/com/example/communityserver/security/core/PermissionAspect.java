package com.example.communityserver.security.core;

import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.utils.redis.RedisUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 权限验证切面
 * 该切面用于在方法执行前进行权限验证，通过检查用户是否具有所需的权限来决定是否允许访问
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/

@Aspect
@Component
public class PermissionAspect {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 权限验证前置通知方法
     * 在带有@RequiresPermission注解的方法执行前进行权限验证
     *
     * @param requiresPermission 权限注解，包含权限数组和逻辑关系(AND/OR)
     */
    @Before("@annotation(requiresPermission)")
    public void before(RequiresPermission requiresPermission) {

        // 从注解中获取权限数组和逻辑关系
        String[] rolePermissions = requiresPermission.role();
        String[] apiPermissions = requiresPermission.api();
        Logical logical = requiresPermission.logical();

        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户拥有的权限列表

        List<String> userRoles = loginUser.getRoles();

        // 获取role所有的api，去重
        Set<String> userRoleApiPerms = new HashSet<>();

        for (String role : userRoles) {
            String roleApiPermsKey = CacheKeyConstants.PERMISSION_IDENTIFIER_ROLE + role;
            List<String> roleApiPerms = redisUtil.getCacheList(roleApiPermsKey);
            if (roleApiPerms != null) {
                userRoleApiPerms.addAll(roleApiPerms);
            }
        }

        // 根据逻辑关系(AND/OR)判断用户是否具有所需权限
        boolean hasPermission;
        if (logical == Logical.AND) {
            // AND逻辑：用户必须拥有所有权限
            hasPermission = Arrays.stream(apiPermissions).allMatch(userRoleApiPerms::contains);
            if (!hasPermission) {
                hasPermission = Arrays.stream(rolePermissions).allMatch(userRoles::contains);
            }
        } else {
            // OR逻辑：用户只需拥有任一权限
            hasPermission = Arrays.stream(apiPermissions).anyMatch(userRoleApiPerms::contains);
            if (!hasPermission) {
                hasPermission = Arrays.stream(rolePermissions).anyMatch(userRoles::contains);
            }
        }

        // 如果没有权限，抛出权限不足异常
        if (!hasPermission) {
            throw new AccessDeniedException("权限不足");
        }
    }
}

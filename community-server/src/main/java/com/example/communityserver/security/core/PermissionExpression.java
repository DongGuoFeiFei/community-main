package com.example.communityserver.security.core;

import com.example.communityserver.entity.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p>
 * 权限验证表达式
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


@Component("perms")
public class PermissionExpression {

    /**
     * 检查当前认证用户是否拥有指定权限
     *
     * @param permission 需要检查的权限字符串
     * @return 如果用户拥有指定权限返回true，否则返回false
     */

    // TODO: 2025/8/10 使用@PreAuthorize不能像 @RequiresPermission("system:user:view")自定义抛出异常
    //    @RequiresPermission("system:user:view")
    //    @PreAuthorize("@perms.hasPerm('system:user:view')")
    //
    // TODO: 2025/8/11 接口验证时先进行身份验证，后进行接口验证
    public boolean hasPerm(String permission) {
        // 从安全上下文中获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 如果认证信息为空或用户未认证，则直接返回false
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 获取认证主体信息
        Object principal = authentication.getPrincipal();
        // 检查主体是否为LoginUser类型
        if (principal instanceof LoginUser) {
            // 类型转换，获取LoginUser对象
            LoginUser loginUser = (LoginUser) principal;
            // 检查用户权限集合中是否包含指定权限
            return loginUser.getApis().contains(permission);
        }
        // 如果主体不是LoginUser类型，返回false
        return false;
    }

    /**
     * 检查当前登录用户是否拥有任意一个指定的权限
     *
     * @param permissions 可变参数，表示需要检查的权限列表
     * @return 如果用户拥有任意一个指定的权限则返回true，否则返回false
     */
    public boolean hasAnyPerm(String... permissions) {
        // 如果权限列表为空或长度为0，直接返回false
        if (permissions == null || permissions.length == 0) {
            return false;
        }

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 如果认证信息为空或用户未认证，返回false
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 获取用户主体信息
        Object principal = authentication.getPrincipal();
        // 如果用户主体是LoginUser类型，则检查权限
        if (principal instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) principal;
            // 使用Stream API检查用户权限列表中是否包含任意一个指定的权限
            return Arrays.stream(permissions)
                    .anyMatch(loginUser.getApis()::contains);
        }

        // 如果用户主体不是LoginUser类型，返回false
        return false;
    }

    /**
     * 检查当前登录用户是否拥有所有指定的权限
     *
     * @param permissions 可变参数，表示需要检查的权限列表
     * @return 如果用户拥有所有指定的权限则返回true，否则返回false
     */
    public boolean hasAllPerm(String... permissions) {
        // 如果权限列表为空或长度为0，直接返回false
        if (permissions == null || permissions.length == 0) {
            return false;
        }

        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 如果认证信息为空或用户未认证，返回false
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // 获取用户主体信息
        Object principal = authentication.getPrincipal();
        // 如果主体是LoginUser类型，则检查权限
        if (principal instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) principal;
            // 使用Stream API检查所有权限是否都包含在用户的权限集合中
            return Arrays.stream(permissions)
                    .allMatch(loginUser.getApis()::contains);
        }

        // 如果主体不是LoginUser类型，返回false
        return false;
    }
}

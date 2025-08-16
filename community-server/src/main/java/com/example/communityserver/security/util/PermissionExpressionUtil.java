package com.example.communityserver.security.util;

import com.example.communityserver.entity.model.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 权限验证表达式
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


@Component("perms")
public class PermissionExpressionUtil {

    /**
     * 检查角色列表中是否包含任何指定的角色
     *
     * @param roles    需要检查的角色列表
     * @param roleList 已有的角色列表
     * @return 如果roles列表中任何一个角色存在于roleList中，则返回true；否则返回false
     */
    public static Boolean hasAnyRolePerm(List<String> roles, List<String> roleList) {
        // 如果权限列表为空或长度为0，直接返回false
        if (roles.isEmpty()) {
            return false;
        }
        return roles.stream().anyMatch(roleList::contains);
    }

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
    public static boolean hasPerm(String permission) {
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
    public static boolean hasAnyPerm(String... permissions) {
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
    public static boolean hasAllPerm(String... permissions) {
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

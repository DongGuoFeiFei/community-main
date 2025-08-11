package com.example.communityserver.security.core;

import com.example.communityserver.entity.model.LoginUser;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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

    // TODO: 2025/8/11 先验证再基础角色权限上是否有着新增或禁止接口 后进行菜单验证（管理员进行该步骤，用户不需要） 后进行角色身份验证
    // 整个流程，主要还是角色验证
    // 角色有着最基础的权限，但可以增加接口数量

    /**
     * 权限验证前置通知方法
     * 在带有@RequiresPermission注解的方法执行前进行权限验证
     *
     * @param requiresPermission 权限注解，包含权限数组和逻辑关系(AND/OR)
     */
    @Before("@annotation(requiresPermission)")
    public void before(RequiresPermission requiresPermission) {
        // 从注解中获取权限数组和逻辑关系
        String[] permissions = requiresPermission.value();
        Logical logical = requiresPermission.logical();

        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户拥有的权限列表
        List<String> userPermissions = loginUser.getApis();

        // 根据逻辑关系(AND/OR)判断用户是否具有所需权限
        boolean hasPermission;
        if (logical == Logical.AND) {
            // AND逻辑：用户必须拥有所有权限
            hasPermission = Arrays.stream(permissions)
                    .allMatch(userPermissions::contains);
        } else {
            // OR逻辑：用户只需拥有任一权限
            hasPermission = Arrays.stream(permissions)
                    .anyMatch(userPermissions::contains);
        }

        // 如果没有权限，抛出权限不足异常
        if (!hasPermission) {
            throw new AccessDeniedException("权限不足");
        }
    }
}

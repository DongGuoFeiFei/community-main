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
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-09
 **/


@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(requiresPermission)")
    public void before(RequiresPermission requiresPermission) {
        String[] permissions = requiresPermission.value();
        Logical logical = requiresPermission.logical();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> userPermissions = loginUser.getPermissions();

        boolean hasPermission;
        if (logical == Logical.AND) {
            hasPermission = Arrays.stream(permissions)
                    .allMatch(userPermissions::contains);
        } else {
            hasPermission = Arrays.stream(permissions)
                    .anyMatch(userPermissions::contains);
        }

        if (!hasPermission) {
            throw new AccessDeniedException("权限不足");
        }
    }
}

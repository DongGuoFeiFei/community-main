package com.example.communityserver.security.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 自定义权限注解
 *
 * @RequiresPermission(value = {"system:user:add", "system:user:edit"}, logical = Logical.OR)
 * @RequiresPermission("system:user:view")
 * 使用方式
 * <p>
 * @author: DongGuo
 * @create: 2025-08-09
 **/

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    String[] value(); // 权限字符串数组

    Logical logical() default Logical.AND; // AND表示需要所有权限，OR表示任一权限
}


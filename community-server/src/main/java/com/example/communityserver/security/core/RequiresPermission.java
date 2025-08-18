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
 * @RequiresPermission("system:user:view") 使用方式
 * <p>
 * @author: DongGuo
 * @create: 2025-08-09
 **/

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    /**
     * 角色权限标识，支持多个
     */
    String[] role() default {};

    /**
     * API权限标识，支持多个
     */
    String[] api() default {};

    /**
     * 菜单权限标识，支持多个
     */
    String[] menu() default {};

    Logical logical() default Logical.AND;
}

package com.example.communityserver.utils;

import com.example.communityserver.entity.po.LoginUser;
import com.example.communityserver.entity.po.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * SecurityUtils - 安全相关工具类
 * 提供用户信息获取、权限检查、密码加密验证等功能
 * 工具类仅编写，尚未验证正确性，需后期测试得知
 * <p>
 *
 *
 * @author: DongGuo
 * @create: 2025-04-29
 **/
public class SecurityUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 获取当前认证上下文
     *
     * @return 当前认证对象{@link Authentication}，未认证返回null
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    /**
     * 获取当前登录用户完整信息
     *
     * @return {@link LoginUser}对象，包含用户详情和权限信息，未登录返回null
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser
                ? (LoginUser) authentication.getPrincipal()
                : null;
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前登录用户名，未登录返回null
     */
    public static String getCurrentUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID，未登录返回null
     * @see User#getUserId()
     */
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUser().getUserId() : null;
    }


    /**
     * 获取当前用户详细信息
     *
     * @return {@link User}实体对象，未登录返回null
     */
    public static User getCurrentUserDetail() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUser() : null;
    }

    /**
     * 检查当前用户是否拥有指定权限
     *
     * @param authority 权限标识符(如："user:create")
     * @return 拥有该权限返回true，否则返回false
     * @see LoginUser#getPermissions()
     */
    public static boolean hasAuthority(String authority) {
        LoginUser loginUser = getLoginUser();
        return loginUser != null && loginUser.getPermissions().contains(authority);
    }

    /**
     * 检查当前用户是否拥有任意指定权限
     *
     * @param authorities 权限标识符数组(如：{"user:read", "user:write"})
     * @return 拥有任一权限返回true，否则返回false
     */
    public static boolean hasAnyAuthority(String... authorities) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        for (String authority : authorities) {
            if (loginUser.getPermissions().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对明文密码进行加密处理
     *
     * @param rawPassword 明文密码
     * @return BCrypt加密后的密码字符串
     * @throws IllegalArgumentException 如果输入密码为空
     */
    public static String encryptPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 验证明文密码与加密密码是否匹配
     *
     * @param rawPassword 待验证的明文密码
     * @param encodedPassword 存储的加密密码
     * @return 匹配返回true，否则返回false
     * @throws IllegalArgumentException 如果任一参数为空
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            throw new IllegalArgumentException("密码参数不能为空");
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
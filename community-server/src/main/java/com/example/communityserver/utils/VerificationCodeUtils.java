package com.example.communityserver.utils;

import java.util.Random;

/**
 * <p>
 * 验证码生成工具
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

public class VerificationCodeUtils {

    // 验证码字符集（去除了容易混淆的字符，如 0/O, 1/I）
    private static final String CODE_CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final Random random = new Random();

    /**
     * 生成6位数字+字母验证码
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CODE_CHARS.charAt(random.nextInt(CODE_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * 生成4位纯数字验证码（简单版）
     */
    public static String generateNumericCode() {
        return String.format("%04d", random.nextInt(10000));
    }
}
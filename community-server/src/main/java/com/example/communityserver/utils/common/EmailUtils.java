package com.example.communityserver.utils.common;

/**
 * <p>
 * 电子邮件工具类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-28
 **/


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class EmailUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private static final String GRAVATAR_URL = "https://www.gravatar.com/avatar/";
    private static final String DEFAULT_GRAVATAR_IMAGE = "identicon";

    /**
     * 验证邮箱格式是否有效
     *
     * @param email 待验证的邮箱地址
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 从邮箱地址提取用户名部分
     *
     * @param email 邮箱地址
     * @return 用户名部分
     */
    public static String extractUsername(String email) {
        if (!isValidEmail(email)) {
            return "";
        }
        return email.substring(0, email.indexOf('@'));
    }

    /**
     * 从邮箱地址提取域名部分
     *
     * @param email 邮箱地址
     * @return 域名部分
     */
    public static String extractDomain(String email) {
        if (!isValidEmail(email)) {
            return "";
        }
        return email.substring(email.indexOf('@') + 1);
    }

    /**
     * 获取邮箱服务提供商名称
     *
     * @param email 邮箱地址
     * @return 服务商名称
     */
    public static String getEmailProvider(String email) {
        String domain = extractDomain(email).toLowerCase();

        if (domain.contains("gmail")) return "Google";
        if (domain.contains("qq")) return "QQ Mail";
        if (domain.contains("163") || domain.contains("126")) return "NetEase";
        if (domain.contains("outlook") || domain.contains("hotmail") || domain.contains("live")) return "Microsoft";
        if (domain.contains("yahoo")) return "Yahoo";
        if (domain.contains("icloud")) return "Apple";
        if (domain.contains("protonmail")) return "ProtonMail";

        return domain;
    }

    /**
     * 获取Gravatar头像URL
     *
     * @param email 邮箱地址
     * @param size  头像尺寸(像素)
     * @return Gravatar URL
     */
    public static String getGravatarUrl(String email, int size) {
        return getGravatarUrl(email, size, DEFAULT_GRAVATAR_IMAGE, false);
    }

    /**
     * 获取Gravatar头像URL（带更多选项）
     *
     * @param email        邮箱地址
     * @param size         头像尺寸(像素)
     * @param defaultImage 默认头像类型
     * @param forceDefault 是否强制使用默认头像
     * @return Gravatar URL
     */
    public static String getGravatarUrl(String email, int size, String defaultImage, boolean forceDefault) {
        if (!isValidEmail(email)) {
            return String.format("%s?s=%d&d=%s", GRAVATAR_URL, size, defaultImage);
        }

        try {
            String hash = md5Hex(email.trim().toLowerCase());
            StringBuilder url = new StringBuilder(GRAVATAR_URL)
                    .append(hash)
                    .append("?s=").append(size)
                    .append("&d=").append(defaultImage);

            if (forceDefault) {
                url.append("&f=y");
            }

            return url.toString();
        } catch (NoSuchAlgorithmException e) {
            return String.format("%s?s=%d&d=%s", GRAVATAR_URL, size, defaultImage);
        }
    }

    /**
     * 生成邮箱的MD5哈希值
     *
     * @param input 输入字符串
     * @return MD5哈希值
     * @throws NoSuchAlgorithmException
     */
    private static String md5Hex(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 隐藏邮箱地址的部分字符（保护隐私）
     *
     * @param email 邮箱地址
     * @return 隐藏后的邮箱地址
     */
    public static String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return "";
        }

        String username = extractUsername(email);
        String domain = extractDomain(email);

        if (username.length() <= 2) {
            // Java 8 兼容方案
            char[] stars = new char[username.length()];
            Arrays.fill(stars, '*');
            return new String(stars) + "@" + domain;
        }

        String maskedUsername = username.charAt(0) +
                repeat('*', username.length() - 2) +
                username.charAt(username.length() - 1);

        return maskedUsername + "@" + domain;
    }


    /**
     * 检查是否为临时/一次性邮箱
     *
     * @param email 邮箱地址
     * @return 是否为临时邮箱
     */
    public static boolean isDisposableEmail(String email) {
        String domain = extractDomain(email).toLowerCase();

        // 常见临时邮箱域名列表（可根据需要扩展）
        String[] disposableDomains = {
                "mailinator.com", "tempmail.com", "10minutemail.com",
                "guerrillamail.com", "yopmail.com", "trashmail.com",
                "temp-mail.org", "fakeinbox.com", "maildrop.cc"
        };

        for (String disposable : disposableDomains) {
            if (domain.equals(disposable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 标准化邮箱地址（小写+去除前后空格）
     *
     * @param email 邮箱地址
     * @return 标准化后的邮箱地址
     */
    public static String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase();
    }

    private static String repeat(char c, int count) {
        char[] array = new char[count];
        Arrays.fill(array, c);
        return new String(array);
    }
}
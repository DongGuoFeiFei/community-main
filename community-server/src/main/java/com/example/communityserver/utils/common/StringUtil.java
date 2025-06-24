package com.example.communityserver.utils.common;

import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    // 正则表达式：匹配<script>标签及其内容
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    // 正则表达式：匹配<style>标签及其内容
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";

    /**
     * 移除HTML标签（将尖括号替换为中文符号）
     *
     * @param content 包含HTML的输入字符串
     * @return 处理后的安全字符串
     */
    public static String removeHtml(String content) {
        if (content == null) {
            return null;
        }
        return content.replace("<", "《").replace(">", "》");
    }

    /**
     * 检查字符串是否包含另一个字符串（不区分大小写）
     *
     * @param text       被检查的文本
     * @param searchText 要搜索的文本
     * @return 如果包含返回true，否则返回false
     */
    public static boolean matchString(String text, String searchText) {
        // 1. 将搜索文本转换为正则表达式（自动转义特殊字符）
        Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
        // 2. 在文本中匹配正则
        Matcher matcher = pattern.matcher(text);
        // 3. 返回是否找到匹配
        return matcher.find();
    }

    /**
     * 检查文件名是否符合命名规则
     *
     * @param fileName 要检查的文件名
     * @return 如果合法返回true，否则返回false
     */
    public static boolean isValidFileName(String fileName) {
        if (!StringUtils.hasText(fileName) || fileName.length() > 128) {
            return false;
        }
        // 允许字母、数字、下划线、点号和连字符，但不能以点号、下划线和连字符开头
        String regex = "^(?![-._])[a-zA-Z0-9-._]+$";
        return fileName.matches(regex);
    }

    /**
     * 检查文件夹名是否符合命名规则
     *
     * @param directoryName 要检查的文件夹名
     * @return 如果合法返回true，否则返回false
     */
    public static boolean isValidDirectoryName(String directoryName) {
        if (!StringUtils.hasText(directoryName) || directoryName.length() > 128) {
            return false;
        }
        // 只允许字母、数字、下划线和连字符
        String regex = "^[a-zA-Z0-9-_]+$";
        return directoryName.matches(regex);
    }

    /**
     * 生成随机UUID字符串（不带横线）
     *
     * @return 32字符的UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带随机前缀的文件名（保留原扩展名）
     *
     * @param originalName 原始文件名
     * @return 带UUID前缀的新文件名
     */
    public static String generateRandomFileName(String originalName) {
        if (!StringUtils.hasText(originalName)) {
            return generateUUID();
        }

        // 获取文件扩展名
        String ext = "";
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex > 0) {
            ext = originalName.substring(dotIndex);
        }

        return generateUUID() + ext;
    }

    /**
     * 检查字符串是否为有效的手机号（中国）
     *
     * @param phone 手机号码字符串
     * @return 如果是有效手机号返回true，否则返回false
     */
    public static boolean isValidChinesePhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        // 中国手机号正则：1开头，第二位3-9，后面9位数字
        String regex = "^1[3-9]\\d{9}$";
        return phone.matches(regex);
    }

    /**
     * 检查字符串是否为有效的邮箱地址
     *
     * @param email 邮箱地址字符串
     * @return 如果是有效邮箱返回true，否则返回false
     */
    public static boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        // 邮箱地址正则（简化版）
        String regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

    /**
     * 隐藏手机号中间四位（保护隐私）
     *
     * @param phone 完整手机号
     * @return 隐藏中间四位后的手机号
     */
    public static String hidePhoneNumber(String phone) {
        if (!isValidChinesePhone(phone)) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 检查字符串是否为空（包括null、空字符串和纯空格）
     *
     * @param str 要检查的字符串
     * @return 如果为空返回true，否则返回false
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 检查字符串是否不为空
     *
     * @param str 要检查的字符串
     * @return 如果不为空返回true，否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 截断字符串，如果超过指定长度则添加省略号
     *
     * @param str       原始字符串
     * @param maxLength 最大长度
     * @return 截断后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}
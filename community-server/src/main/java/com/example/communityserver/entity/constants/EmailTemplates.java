package com.example.communityserver.entity.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 验证码邮件模板（HTML格式）
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


public class EmailTemplates {
    public static final String LOGIN_URL = "http://localhost:5173/login";
    public static final String SUPPORT_EMAIL_ADDRESS = "admin@foxmail.com";
    public static final String SUBJECT_WELCOME = "欢迎注册【采芙蓉】";
    public static final String SUBJECT_VERIFICATION = "【采芙蓉】邮箱验证码";
    public static final String SUBJECT_RESET_PASSWORD = "【采芙蓉】邮箱重置密码";

    // 验证码邮件模板
    private static final String VERIFICATION_TEMPLATE =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>邮箱验证</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; line-height: 1.6; }\n" +
                    "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                    "        .code { font-size: 24px; color: #1890ff; font-weight: bold; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <p>尊敬的 <b>${username}</b>：</p>\n" +
                    "        <p>感谢您注册我们的服务！您的验证码为：</p>\n" +
                    "        <p class=\"code\">${code}</p>\n" +
                    "        <p>验证码将在 <b>5分钟</b> 后失效，请尽快完成验证。</p>\n" +
                    "        <p>如非本人操作，请忽略此邮件。</p>\n" +
                    "        <hr>\n" +
                    "        <p style=\"color: #888;\">系统自动发送，请勿直接回复</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

    // 欢迎邮件模板
    private static final String WELCOME_TEMPLATE =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>欢迎加入</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div style=\"max-width: 600px; margin: 0 auto;\">\n" +
                    "        <h2 style=\"color: #1890ff;\">欢迎，${username}！</h2>\n" +
                    "        <p>您的账号已成功注册，立即登录体验服务:</p>\n" +
                    "        <a href=\"${loginUrl}\" style=\"display: inline-block; padding: 10px 20px; background: #1890ff; color: white; text-decoration: none;\">\n" +
                    "            立即登录\n" +
                    "        </a>\n" +
                    "        <p>如有任何问题，请联系客服：${supportEmail}</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

    private static final String RESET_PASSWORD_TEMPLATE =
            "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>重置密码</title>\n" +
                    "    <style>\n" +
                    "        body { font-family: Arial, sans-serif; line-height: 1.6; }\n" +
                    "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                    "        .button { display: inline-block; padding: 10px 20px; background: #1890ff; color: white; text-decoration: none; }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h2 style=\"color: #1890ff;\">重置密码</h2>\n" +
                    "        <p>尊敬的用户${username}：</p>\n" +
                    "        <p>您正在请求重置账户密码成：${password}，请点击下方按钮进行密码重置：</p>\n" +
                    "        <a href=\"${resetUrl}\" class=\"button\">重置密码</a>\n" +
                    "        <p>如果按钮无法点击，请复制以下链接到浏览器地址栏：</p>\n" +
                    "        <p>${resetUrl}</p>\n" +
                    "        <p>此链接将在 <b>30分钟</b> 后失效。</p>\n" +
                    "        <p>如非本人操作，请忽略此邮件。</p>\n" +
                    "        <hr>\n" +
                    "        <p style=\"color: #888;\">系统自动发送，请勿直接回复</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

    /**
     * 替换模板中的占位符
     */
    private static String renderTemplate(String template, Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }


    /**
     * 渲染验证码邮件
     */
    public static String getRenderVerificationEmail(String username, String code) {
        Map<String, String> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("code", code);
        return renderTemplate(VERIFICATION_TEMPLATE, variables);
    }

    /**
     * 渲染欢迎邮件
     */
    public static String getRenderWelcomeEmail(String username) {
        Map<String, String> variables = new HashMap<>();
        variables.put("username", username);
        variables.put("loginUrl", LOGIN_URL);
        variables.put("supportEmail", SUPPORT_EMAIL_ADDRESS);
        return renderTemplate(WELCOME_TEMPLATE, variables);
    }


    // 修改 getResetPasswordEmail 方法实现
    public static String getResetPasswordEmail(String token, String username, String password) {
        Map<String, String> variables = new HashMap<>();
        String resetUrl = SystemConstants.BASIC_URL + "/auth/reset-password/" + token;
        variables.put("resetUrl", resetUrl);
        variables.put("username", username);
        variables.put("password", password);
        return renderTemplate(RESET_PASSWORD_TEMPLATE, variables);
    }
}

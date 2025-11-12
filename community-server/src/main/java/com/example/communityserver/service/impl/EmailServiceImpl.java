package com.example.communityserver.service.impl;

import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.EmailTemplates;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.utils.common.StringUtil;
import com.example.communityserver.utils.common.VerificationCodeUtils;
import com.example.communityserver.utils.redis.RedisUtil;
import org.apache.catalina.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 邮箱
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Service
public class EmailServiceImpl implements IEmailService {
    // Redis键前缀
    private static final String REDIS_VERIFY_CODE_PREFIX = "verify:email:";
    // 验证码有效期（分钟）
    private static final int VERIFY_CODE_EXPIRE_MINUTES = 5;
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 发送验证码邮件
     */
    @Async
    @Retryable(
            value = {MailException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendVerificationCode(String toEmail, String username) {
        try {
            // 1. 生成验证码
            String code = VerificationCodeUtils.generateNumericCode();
            log.info("生成验证码: email={}, code={}", toEmail, code);

            // 2. 存储到Redis（5分钟过期）
            redisUtil.setCacheObject(
                    REDIS_VERIFY_CODE_PREFIX + toEmail,
                    code,
                    VERIFY_CODE_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );

            // 3. 发送邮件
            String content = EmailTemplates.getRenderVerificationEmail(username, code);
            sendMimeMessage(toEmail, EmailTemplates.SUBJECT_VERIFICATION, content);

        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败，请检查邮箱地址", e);
        } catch (Exception e) {
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
    }

    /**
     * 发送欢迎邮件
     */
    @Async
    @Override
    @Retryable(
            value = {MailException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            String content = EmailTemplates.getRenderWelcomeEmail(username);
            sendMimeMessage(toEmail, EmailTemplates.SUBJECT_WELCOME, content);
            log.info("欢迎邮件发送成功: email={}", toEmail);
        } catch (MessagingException e) {
            log.error("欢迎邮件发送失败: email={}, error={}", toEmail, e.getMessage());
        }
    }


    @Override
    public boolean sendResetPassword(String email, String username) {
        // 给用户发送邮箱，重置链接，链接内容是一个服务器的get请求链接，链接包含和重置邮箱绑定的token
        // 链接会返回一个重置成功的页面
        String value = StringUtil.generateUUID();
        String key = CacheKeyConstants.RESET_PASSWORD_KEY + value;
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("email", email);
        String password = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        valueMap.put("password", password);
        redisUtil.setCacheObject(key, valueMap, 30, TimeUnit.MINUTES);
        try {
            String content = EmailTemplates.getResetPasswordEmail(value, username, password);
            sendMimeMessage(email, EmailTemplates.SUBJECT_RESET_PASSWORD, content);
            log.info("重置密码邮件发送成功: email={}", email);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * 校验验证码返回的注册验证码是否正确
     */
    public boolean verifyCode(String email, String code) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            return false;
        }

        String storedCode = redisUtil.getCacheObject(REDIS_VERIFY_CODE_PREFIX + email);
        boolean isValid = code.equals(storedCode);

        if (isValid) {
            // 验证通过后删除Redis中的验证码（防止重复使用）
            redisUtil.deleteObject(REDIS_VERIFY_CODE_PREFIX + email);
            log.info("验证码校验成功: email={}", email);
        } else {
            log.warn("验证码校验失败: email={}", email);
        }

        return isValid;
    }

    public String getEmailGravatarUrl(String email) {
        try {
            // 计算邮箱的MD5哈希值
            String hash = md5Hex(email.trim().toLowerCase());
//            String emailHash = DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes());
//            return "https://www.gravatar.com/avatar/" + emailHash + "?d=identicon";
            return "https://www.gravatar.com/avatar/" + hash + "?d=identicon";
        } catch (Exception e) {
            return "https://www.gravatar.com/avatar/?d=identicon";
        }
    }

    private String md5Hex(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * @Description: 通用MIME邮件发送方法
     * @Param: [toEmail(目标邮箱), subject(邮件主题), content(邮箱文本内容)]
     * @return: void
     * @Author: DongGuo
     */

    private void sendMimeMessage(String toEmail, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

}

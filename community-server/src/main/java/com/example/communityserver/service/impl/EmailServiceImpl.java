package com.example.communityserver.service.impl;

import com.example.communityserver.entity.constants.EmailTemplates;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.utils.common.VerificationCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private static final long VERIFY_CODE_EXPIRE_MINUTES = 5;
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${spring.mail.username}")
    private String fromEmail;

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
            redisTemplate.opsForValue().set(
                    REDIS_VERIFY_CODE_PREFIX + toEmail,
                    code,
                    VERIFY_CODE_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );

            // 3. 发送邮件
            String content = EmailTemplates.getRenderVerificationEmail(username, code);
            sendMimeMessage(toEmail, EmailTemplates.SUBJECT_VERIFICATION, content);

            log.info("验证码邮件发送成功: email={}", toEmail);
        } catch (MessagingException e) {
            log.error("邮件构建失败: email={}, error={}", toEmail, e.getMessage());
            throw new RuntimeException("邮件发送失败，请检查邮箱地址", e);
        } catch (Exception e) {
            log.error("未知错误: email={}, error={}", toEmail, e.getMessage());
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

    /**
     * 校验验证码返回的注册验证码是否正确
     */
    public boolean verifyCode(String email, String code) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(code)) {
            return false;
        }

        String storedCode = redisTemplate.opsForValue().get(REDIS_VERIFY_CODE_PREFIX + email);
        boolean isValid = code.equals(storedCode);

        if (isValid) {
            // 验证通过后删除Redis中的验证码（防止重复使用）
            redisTemplate.delete(REDIS_VERIFY_CODE_PREFIX + email);
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
     * 通用MIME邮件发送方法
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

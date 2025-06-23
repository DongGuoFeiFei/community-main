package com.example.communityserver.service;

import javax.mail.MessagingException;

/**
 * <p>
 * 邮箱
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


public interface IEmailService {
    public void sendVerificationCode(String toEmail, String username) ;
    public void sendWelcomeEmail(String toEmail, String username) ;
}

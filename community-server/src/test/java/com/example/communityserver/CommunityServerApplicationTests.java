package com.example.communityserver;

import com.example.communityserver.entity.po.LoginUser;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.utils.JWTUtil;
import com.example.communityserver.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.communityserver.utils.SecurityUtils.getLoginUser;

@SpringBootTest
class CommunityServerApplicationTests {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ICommentService commentService;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void sendEmail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fcwbebe@foxmail.com"); // 必须和yml中的username一致
        message.setTo("2422516737@qq.com");
        message.setSubject("Test");
        message.setText("Hello from Foxmail!");
        mailSender.send(message);
    }

    @Test
    void contextLoads() {
        System.out.println(commentService.getById(1));
    }

    @Test
    void password() {
        // 生成加密的密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Test
    void decryptToken() {
        System.out.println(JWTUtil.getUserId("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3NDU5NDY2ODEsInVzZXJJZCI6MX0.wEzrXolizjmfth3oaet0ai85e_jCZZ2SYnzl_jISckI"));

        LoginUser loginUser = redisUtil.getCacheObject("user:1");
        System.out.println(loginUser);
    }

    @Test
    void getLoginUserTest() {
        System.out.println(getLoginUser());
    }

}

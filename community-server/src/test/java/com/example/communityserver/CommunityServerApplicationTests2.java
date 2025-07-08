package com.example.communityserver;

import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.service.ICommentService;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.utils.security.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static com.example.communityserver.utils.security.SecurityUtils.getLoginUser;

@SpringBootTest
class CommunityServerApplicationTests2 {

    private static final String API_URL = "https://v.api.aa1.cn/api/ipcha-baidu/?ip=";

    @Test
    void contextLoads() {
        getIpLocation("223.160.222.112");
    }

    public String getIpLocation(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + ip, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get IP location: " + response.getStatusCode());
        }
    }

}

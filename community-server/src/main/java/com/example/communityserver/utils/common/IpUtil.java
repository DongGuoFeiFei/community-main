package com.example.communityserver.utils.common;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Component
public class IpUtil {
    private static final String API_URL = "https://v.api.aa1.cn/api/ipcha-baidu/?ip=";
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

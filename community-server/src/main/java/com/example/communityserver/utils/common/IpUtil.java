package com.example.communityserver.utils.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 获取IP数据工具类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Component
public class IpUtil {
    private static final String API_URL = "https://v.api.aa1.cn/api/ipcha-baidu/?ip=";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getIpLocation(String ip) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL + ip, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get IP location: " + response.getStatusCode());
        }
    }

    public IpLocation parseWithJackson(String jsonStr) {
        // 防御性编程
        try {
            return objectMapper.readValue(jsonStr, IpLocation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }

    @Data // Lombok 注解
    public static class IpLocation {
        private int code;
        @JsonProperty("ip_add")
        private String ipAdd;
    }
}

package com.example.communityserver.utils.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@Component
public class JacksonParserUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IpLocation parseWithJackson(String jsonStr){
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

package com.example.communityserver.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/


@Data
public class DeepSeekRequest {
    private String model;
    private List<Message> messages;
    private Integer max_tokens;
    private Double temperature;

    @Data
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
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
    // 模型
    private String model;
    // 消息列表
    private List<Message> messages;
    // 最大令牌数
    private Integer max_tokens;
    // 温度
    private Double temperature;

    @Data
    @AllArgsConstructor
    public static class Message {
        // 角色
        private String role;
        // 内容
        private String content;
    }
}
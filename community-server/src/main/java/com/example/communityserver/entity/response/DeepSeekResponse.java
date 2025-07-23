package com.example.communityserver.entity.response;

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
public class DeepSeekResponse {
    // 响应ID
    private String id;
    // 响应对象
    private String object;
    // 响应创建时间
    private Long created;
    // 响应模型
    private String model;
    // 响应选项
    private List<Choice> choices;
    // 响应使用情况
    private Usage usage;
    // 系统指纹
    private String system_fingerprint;

    @Data
    public static class Choice {
        // 选项索引
        private Integer index;
        // 选项消息
        private Message message;
        // 选项对数概率
        private Object logprobs;
        // 完成原因
        private String finish_reason;
    }


    @Data
    public static class Usage {
        // 提示令牌数
        private Integer prompt_tokens;
        // 完成令牌数
        private Integer completion_tokens;
        // 总令牌数
        private Integer total_tokens;
        // 提示令牌详情
        private PromptTokensDetails prompt_tokens_details;
        // 提示缓存命中令牌数
        private Integer prompt_cache_hit_tokens;
        // 提示缓存未命中令牌数
        private Integer prompt_cache_miss_tokens;
    }

    @Data
    public static class Message {
        // 角色
        private String role;
        // 内容
        private String content;
    }

    @Data
    public static class PromptTokensDetails {
        // 缓存令牌数
        private Integer cached_tokens;
    }
}
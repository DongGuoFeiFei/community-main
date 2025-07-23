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
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String system_fingerprint;

    @Data
    public static class Choice {
        private Integer index;
        private Message message;
        private Object logprobs;
        private String finish_reason;
    }


    @Data
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
        private PromptTokensDetails prompt_tokens_details;
        private Integer prompt_cache_hit_tokens;
        private Integer prompt_cache_miss_tokens;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    public static class PromptTokensDetails {
        private Integer cached_tokens;
    }
}
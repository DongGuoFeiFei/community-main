package com.example.communityserver.service;

import com.example.communityserver.entity.response.DeepSeekResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 接入deepseek
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/


public interface IDeepSeekService {
    DeepSeekResponse generateText(String prompt);

    String generateTextWithHistory(List<Map<String, String>> messages);

    String parseChatContent(DeepSeekResponse response);
}

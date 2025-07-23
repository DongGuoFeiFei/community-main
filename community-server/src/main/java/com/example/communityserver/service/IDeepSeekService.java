package com.example.communityserver.service;

import com.example.communityserver.entity.response.DeepSeekResponse;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/


public interface IDeepSeekService {
    public DeepSeekResponse generateText(String prompt);

    public String generateTextWithHistory(List<Map<String, String>> messages);
}

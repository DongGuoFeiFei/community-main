package com.example.communityserver.service.impl;

import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.request.DeepSeekRequest;
import com.example.communityserver.entity.response.DeepSeekResponse;
import com.example.communityserver.handler.InsufficientBalanceException;
import com.example.communityserver.service.IDeepSeekService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

@Service
public class DeepSeekService implements IDeepSeekService {
    private final RestTemplate restTemplate;

    @Value("${deepseek.api.key}")
    private String apiKey;
    @Value("${deepseek.api.url}")
    private String apiUrl;

    // 构造函数，初始化RestTemplate对象
    public DeepSeekService() {
        this.restTemplate = new RestTemplate();
    }

    public DeepSeekResponse generateText(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            DeepSeekRequest request = new DeepSeekRequest();
            request.setModel("deepseek-chat");
            request.setMessages(List.of(new DeepSeekRequest.Message("user", SystemConstants.DEEPSEEK_PROMPT + prompt)));
            request.setMax_tokens(1024);
            request.setTemperature(0.7);
            HttpEntity<DeepSeekRequest> requestEntity = new HttpEntity<>(request, headers);

            // 发送请求
            ResponseEntity<DeepSeekResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    DeepSeekResponse.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.PAYMENT_REQUIRED) {
                throw new InsufficientBalanceException("DeepSeek API余额不足，请充值");
            }
            throw new RuntimeException("调用DeepSeek API失败", e);
        } catch (Exception e) {
            throw new RuntimeException("调用DeepSeek API失败", e);
        }
    }

    public String generateTextWithHistory(List<Map<String, String>> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 2048);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody();
    }

    /**
     * @Description: 解析deepseek返回数据类型，解析出返回第一条内容
     * @Param: [response]
     * @return: java.lang.String
     * @Author: DongGuo
     */

    public String parseChatContent(DeepSeekResponse response) {
        if (response.getChoices() == null || response.getChoices().isEmpty()) {
            return null;
        }

        String content = response.getChoices().get(0).getMessage().getContent();
        // 处理可能存在的代码块标记
        content = content.replace("```json", "").replace("```", "").trim();
        return content;
    }


}

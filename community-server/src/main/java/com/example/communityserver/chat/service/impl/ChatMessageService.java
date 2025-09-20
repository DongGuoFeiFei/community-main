package com.example.communityserver.chat.service.impl;

import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.entity.request.ChatMessage;
import com.example.communityserver.chat.mapper.ImMessageMapper;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 聊天消息服务实现类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-30
 **/

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final RedisTemplate<String, Object> redisTemplate;
    // 批量插入队列（线程安全）
    private final BlockingQueue<ImMessage> messageQueue = new LinkedBlockingQueue<>(1000);
    @Autowired
    private ImMessageMapper messageMapper;

    /**
     * 初始化方法，使用@PostConstruct注解标注，表示在Bean属性设置完成后执行
     * 该方法启动一个定时任务，用于定期批量插入消息数据
     */
    @PostConstruct
    public void init() {
        // 启动定时批量插入任务，延迟5秒后每5秒执行一次批量插入
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::batchInsertMessages, 5, 5, TimeUnit.SECONDS);
    }

    /**
     * 异步保存消息（内存队列+定时批量插入）
     */
    public void asyncSaveMessage(ChatMessage chatMessage) {
        // 1. 先存Redis（毫秒级响应）
        cacheMessageToRedis(chatMessage);

        // 2. 加入批量队列
        ImMessage dbMessage = convertToDbMessage(chatMessage);
        if (!messageQueue.offer(dbMessage)) {
            log.warn("Message queue full, direct insert");
            messageMapper.insert(dbMessage); // 降级处理
        }
    }

    /**
     * 批量插入MySQL
     */
    private void batchInsertMessages() {
        List<ImMessage> messages = new ArrayList<>();
        messageQueue.drainTo(messages, 100); // 每次最多取100条

        if (!messages.isEmpty()) {
            try {
                messageMapper.batchInsert(messages);
                log.info("Batch inserted {} messages", messages.size());
            } catch (Exception e) {
                log.error("Batch insert failed", e);
                messages.forEach(msg -> {
                    try {
                        messageMapper.insert(msg);
                    } catch (Exception ex) {
                        log.error("Insert failed for message: {}", msg.getId(), ex);
                    }
                });
            }
        }
    }

    /**
     * 缓存到Redis（最近200条消息）
     */
    private void cacheMessageToRedis(ChatMessage message) {
        String redisKey = CacheKeyConstants.CHAT_SESSION_MESSAGES + message.getSessionId();
        message.setTimestamp(new Date());
        redisTemplate.opsForList().rightPush(redisKey, message);
        // 保留最近的200条消息，修正trim范围为0到199（包含）
        redisTemplate.opsForList().trim(redisKey, -200, -1);
        redisTemplate.expire(redisKey, 3, TimeUnit.DAYS);
    }

    private ImMessage convertToDbMessage(ChatMessage chatMessage) {
        ImMessage message = new ImMessage();
        message.setSessionId(chatMessage.getSessionId());
        // TODO: 2025/8/30  根据senderName查用户ID
        message.setSenderId(null);
        message.setContent(chatMessage.getContent());
        message.setSendTime(chatMessage.getTimestamp());
        return message;
    }
}

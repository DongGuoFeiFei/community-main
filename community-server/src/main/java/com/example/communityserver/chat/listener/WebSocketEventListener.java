package com.example.communityserver.chat.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * WebSocket 事件监听器
 * 监听 WebSocket 连接和断开事件
 * </p>
 *
 * @author: DongGuo
 * @create: 2025-09-03
 **/
@Slf4j
@Component
public class WebSocketEventListener {

    // 使用 AtomicInteger 保证线程安全
    private static final AtomicInteger userNumber = new AtomicInteger(0);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * 监听 WebSocket 连接事件
     * 当用户成功建立 WebSocket 连接时触发
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        
        // 从会话属性中获取用户信息
        Long userId = (Long) accessor.getSessionAttributes().get("userId");
        String username = (String) accessor.getSessionAttributes().get("username");
        
        int currentUserCount = userNumber.incrementAndGet();
        
        // 广播用户上线消息
        messagingTemplate.convertAndSend("/topic/join", currentUserCount);
        
        log.info("WebSocket 连接成功 - 会话ID: {}, 用户ID: {}, 用户名: {}, 当前在线人数: {}", 
                accessor.getSessionId(), userId, username, currentUserCount);
    }

    /**
     * 监听 WebSocket 断开事件
     * 当用户断开 WebSocket 连接时触发
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        
        // 从会话属性中获取用户信息
        Long userId = (Long) accessor.getSessionAttributes().get("userId");
        String username = (String) accessor.getSessionAttributes().get("username");
        
        int currentUserCount = userNumber.decrementAndGet();
        
        // 确保在线人数不为负数
        if (currentUserCount < 0) {
            userNumber.set(0);
            currentUserCount = 0;
        }
        
        // 广播用户离线消息
        messagingTemplate.convertAndSend("/topic/leave", currentUserCount);
        
        log.info("WebSocket 连接断开 - 会话ID: {}, 用户ID: {}, 用户名: {}, 当前在线人数: {}", 
                accessor.getSessionId(), userId, username, currentUserCount);
    }

    /**
     * 获取当前在线人数
     *
     * @return 当前在线人数
     */
    public static int getCurrentUserCount() {
        return userNumber.get();
    }
}

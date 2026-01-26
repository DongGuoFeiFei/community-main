package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.request.RecallMessageRequest;
import com.example.communityserver.chat.entity.request.SendMessageRequest;
import com.example.communityserver.chat.service.ChatMessageService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.entity.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 消息控制器
 * 处理实时消息操作（发送、撤回等）
 * 
 * 注意：认证信息由 WebSocketSecurityConfig 自动恢复，无需手动处理
 *
 * @author DongGuo
 * @since 2025-01-23
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService messageService;

    /**
     * 处理私聊消息
     * 客户端发送到: /app/privateChat.{sessionId}
     * 服务端推送到: /topic/chatRoom.private.{sessionId}
     *
     * @param request   消息请求
     * @param sessionId 会话ID
     * @param accessor  STOMP 头访问器（用于备用获取用户信息）
     */
    @MessageMapping("/privateChat.{sessionId}")
    public void handlePrivateChat(@Payload SendMessageRequest request,
                                   @DestinationVariable Long sessionId,
                                   StompHeaderAccessor accessor) {
        try {
            log.info("收到 WebSocket 消息, sessionId={}, msgType={}, 线程={}", 
                    sessionId, request.getMsgType(), Thread.currentThread().getName());

            // 1. 获取当前用户ID（ExecutorChannelInterceptor.beforeHandle 已恢复认证信息到 SecurityContext）
            Long senderId = getCurrentUserId(accessor);
            if (senderId == null) {
                log.warn("未登录用户尝试发送消息 - 会话ID: {}", accessor.getSessionId());
                sendError(sessionId, "未登录，无法发送消息");
                return;
            }

            // 2. 发送消息（Service 层可以直接使用 SecurityUtils）
            ChatMessage message = messageService.sendMessage(sessionId, senderId, request);

            // 3. 通过 WebSocket 推送给会话内所有成员
            messagingTemplate.convertAndSend(
                    "/topic/chatRoom.private." + sessionId,
                    message
            );

            log.info("消息发送成功: sessionId={}, messageId={}, senderId={}",
                    sessionId, message.getId(), senderId);

        } catch (IllegalArgumentException e) {
            log.warn("消息发送失败: sessionId={}, 原因: {}", sessionId, e.getMessage());
            sendError(sessionId, e.getMessage());
        } catch (Exception e) {
            log.error("处理 WebSocket 消息失败: sessionId={}", sessionId, e);
            sendError(sessionId, "消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 处理消息撤回
     * 客户端发送到: /app/recallMessage.{sessionId}
     * 服务端推送到: /topic/chatRoom.private.{sessionId}
     *
     * @param request   撤回请求
     * @param sessionId 会话ID
     * @param accessor  STOMP 头访问器
     */
    @MessageMapping("/recallMessage.{sessionId}")
    public void handleRecallMessage(@Payload RecallMessageRequest request,
                                     @DestinationVariable Long sessionId,
                                     StompHeaderAccessor accessor) {
        try {
            log.info("收到撤回消息请求, sessionId={}, messageId={}", sessionId, request.getMessageId());

            // 1. 获取当前用户ID
            Long userId = getCurrentUserId(accessor);
            if (userId == null) {
                log.warn("未登录用户尝试撤回消息 - 会话ID: {}", accessor.getSessionId());
                sendError(sessionId, "未登录，无法撤回消息");
                return;
            }

            // 2. 撤回消息
            boolean success = messageService.recallMessage(request.getMessageId(), userId);
            
            if (success) {
                // 3. 通知会话内所有成员消息已撤回
                Map<String, Object> recallNotification = new HashMap<>();
                recallNotification.put("type", "recall");
                recallNotification.put("messageId", request.getMessageId());
                recallNotification.put("sessionId", sessionId);
                recallNotification.put("userId", userId);
                
                messagingTemplate.convertAndSend(
                        "/topic/chatRoom.private." + sessionId,
                        recallNotification
                );

                log.info("消息撤回成功: sessionId={}, messageId={}, userId={}",
                        sessionId, request.getMessageId(), userId);
            }

        } catch (IllegalArgumentException e) {
            log.warn("消息撤回失败: sessionId={}, 原因: {}", sessionId, e.getMessage());
            sendError(sessionId, e.getMessage());
        } catch (Exception e) {
            log.error("处理消息撤回失败: sessionId={}", sessionId, e);
            sendError(sessionId, "消息撤回失败: " + e.getMessage());
        }
    }

    /**
     * 处理群聊消息（预留）
     * 客户端发送到: /app/groupChat.{sessionId}
     * 服务端推送到: /topic/chatRoom.group.{sessionId}
     *
     * @param request   消息请求
     * @param sessionId 会话ID
     * @param accessor  STOMP 头访问器
     */
    @MessageMapping("/groupChat.{sessionId}")
    public void handleGroupChat(@Payload SendMessageRequest request,
                                 @DestinationVariable Long sessionId,
                                 StompHeaderAccessor accessor) {
        try {
            log.info("收到群聊消息, sessionId={}, msgType={}", sessionId, request.getMsgType());

            // 1. 获取当前用户ID
            Long senderId = getCurrentUserId(accessor);
            if (senderId == null) {
                log.warn("未登录用户尝试发送群聊消息 - 会话ID: {}", accessor.getSessionId());
                sendError(sessionId, "未登录，无法发送消息");
                return;
            }

            // 2. 发送消息
            ChatMessage message = messageService.sendMessage(sessionId, senderId, request);

            // 3. 推送给群内所有成员
            messagingTemplate.convertAndSend(
                    "/topic/chatRoom.group." + sessionId,
                    message
            );

            log.info("群聊消息发送成功: sessionId={}, messageId={}, senderId={}",
                    sessionId, message.getId(), senderId);

        } catch (IllegalArgumentException e) {
            log.warn("群聊消息发送失败: sessionId={}, 原因: {}", sessionId, e.getMessage());
            sendError(sessionId, e.getMessage());
        } catch (Exception e) {
            log.error("处理群聊消息失败: sessionId={}", sessionId, e);
            sendError(sessionId, "消息发送失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID
     * 从 SecurityUtils 获取（ExecutorChannelInterceptor.beforeHandle 已恢复认证信息）
     *
     * @param accessor STOMP 头访问器（用于日志）
     * @return 用户ID，如果未登录返回 null
     */
    private Long getCurrentUserId(StompHeaderAccessor accessor) {
        Long userId = SecurityUtils.getLoginUserId();
        if (userId != null) {
            log.debug("从 SecurityUtils 获取用户ID: {}, 线程: {}", userId, Thread.currentThread().getName());
        } else {
            log.warn("无法获取用户ID - 会话ID: {}, 线程: {}", 
                    accessor.getSessionId(), Thread.currentThread().getName());
        }
        return userId;
    }

    /**
     * 发送错误消息到客户端
     *
     * @param sessionId 会话ID
     * @param errorMsg  错误消息
     */
    private void sendError(Long sessionId, String errorMsg) {
        Map<String, Object> error = new HashMap<>();
        error.put("type", "error");
        error.put("message", errorMsg);
        error.put("timestamp", System.currentTimeMillis());
        
        messagingTemplate.convertAndSend(
                "/topic/chatRoom.error." + sessionId,
                error
        );
    }
}

package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.request.ChatMessage;
import com.example.communityserver.chat.service.impl.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService messageService; // 新增服务层

    // 公共聊天消息
    @MessageMapping("/chat.public")
    @SendTo("/topic/public")
    public ChatMessage handlePublicMessage(@Payload ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        message.setTimestamp(LocalDateTime.now());
        message.setMessageId(UUID.randomUUID().toString());
        // 2. 异步保存到数据库（非阻塞）
        messageService.asyncSaveMessage(message);
        log.info("Public message from {}: {}", principal.getName(), message.getContent());
        return message;
    }

    // 私聊消息
    @MessageMapping("/chat.private")
    public void handlePrivateMessage(@Payload ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        message.setTimestamp(LocalDateTime.now());
        message.setMessageId(UUID.randomUUID().toString());
        // 2. 异步保存到数据库（非阻塞）
        messageService.asyncSaveMessage(message);
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/private",
                message
        );
        log.info("Private message from {} to {}: {}", principal.getName(), message.getReceiver(), message.getContent());
    }

    // 用户加入聊天室
    @MessageMapping("/chat.join")
    @SendTo("/topic/public")
    public ChatMessage handleUserJoin(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String username = message.getSender();
        headerAccessor.getSessionAttributes().put("username", username);

        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setType(ChatMessage.MessageType.JOIN);
        joinMessage.setSender("System");
        joinMessage.setContent(username + " joined the chat");
        joinMessage.setTimestamp(LocalDateTime.now());

        log.info("User {} joined the chat", username);
        return joinMessage;
    }

    // 用户离开聊天室
    @MessageMapping("/chat.leave")
    @SendTo("/topic/public")
    public ChatMessage handleUserLeave(@Payload ChatMessage message) {
        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setType(ChatMessage.MessageType.LEAVE);
        leaveMessage.setSender("System");
        leaveMessage.setContent(message.getSender() + " left the chat");
        leaveMessage.setTimestamp(LocalDateTime.now());

        log.info("User {} left the chat", message.getSender());
        return leaveMessage;
    }

    // 输入状态通知
    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing")
    public ChatMessage handleTyping(@Payload ChatMessage message, Principal principal) {
        message.setSender(principal.getName());
        message.setTimestamp(LocalDateTime.now());
        return message;
    }
}

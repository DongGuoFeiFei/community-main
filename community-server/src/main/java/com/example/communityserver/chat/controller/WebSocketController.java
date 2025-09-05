package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.request.ChatMessage;
import com.example.communityserver.chat.service.impl.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService messageService; // 新增服务层

    @MessageMapping("/privateChat.{sessionId}")
    public void singleChat(@Payload ChatMessage message, @DestinationVariable Long sessionId) {
        log.info("收到聊天消息: {}", message);
        log.info("sessionId: {}", sessionId);
        // 发送到公共的聊天室主题（所有订阅该聊天室的用户都会收到）
        messagingTemplate.convertAndSend(
                "/topic/chatRoom.private." + sessionId,
                message
        );

//        // 同时发送给发送者自己（确保发送者也能收到）
//        messagingTemplate.convertAndSendToUser(
//                message.getSenderId().toString(),
//                "/queue/private",
//                message
//        );
    }
}

package com.example.communityserver.chat.controller;

import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.service.ChatMessageService;
import com.example.communityserver.core.security.util.SecurityUtils;
import com.example.communityserver.entity.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ChatMessageService messageService;

    @MessageMapping("/privateChat.{sessionId}")
    public void singleChat(@Payload ChatMessage message, @DestinationVariable Long sessionId) {

        log.info("接收到 WebSocket 消息, sessionId={}, payload={}", sessionId, message);

        // 1. 补全会话信息（以路径变量为准，防止前端伪造）
        message.setSessionId(sessionId);

        // 2. 补全发送者信息，确保与前端 ChatMessage 类型一致
        User currentUser = SecurityUtils.getCurrentUserDetail();
        if (currentUser != null) {
            message.setSenderId(currentUser.getUserId());
            // 优先使用昵称，其次用户名
            message.setSenderName(
                    currentUser.getNickname() != null ? currentUser.getNickname() : currentUser.getUsername());
            message.setSenderAvatar(currentUser.getAvatar());
        }

        // 3. 补全发送时间，和历史消息保持同一字段
        if (message.getSendTime() == null) {
            message.setSendTime(LocalDateTime.now());
        }

        // 4. 异步保存消息，拿到数据库生成的 id / msgSeq 等字段
        messageService.asyncSaveMessage(message);

        // 5. 发送到公共的聊天室主题（所有订阅该聊天室的用户都会收到）
        messagingTemplate.convertAndSend(
                "/topic/chatRoom.private." + sessionId,
                message);

        // 6. 可选：同时发送给当前用户的私有队列（前端如果需要可以订阅 /user/queue/private）
        if (message.getSenderId() != null) {
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/private",
                    message);
        }
    }
}

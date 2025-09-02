package com.example.communityserver.chat.service.impl;

import com.example.communityserver.chat.entity.request.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // 发送系统消息
    public void sendSystemMessage(String content) {
        ChatMessage message = new ChatMessage();
        message.setType(ChatMessage.MessageType.SYSTEM);
        message.setSender("System");
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/system", message);
    }

    // 发送广播消息
    public void broadcastMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/public", message);
    }

    // 发送私聊消息
    public void sendPrivateMessage(String toUser, ChatMessage message) {
        messagingTemplate.convertAndSendToUser(toUser, "/queue/private", message);
    }

    // 发送消息已读回执
    public void sendReadReceipt(String toUser, String messageId) {
        ChatMessage receipt = new ChatMessage();
        receipt.setType(ChatMessage.MessageType.READ_RECEIPT);
        receipt.setSender("System");
        receipt.setContent("Message read");
        receipt.setMessageId(messageId);
        receipt.setTimestamp(LocalDateTime.now());

        messagingTemplate.convertAndSendToUser(toUser, "/queue/receipts", receipt);
    }
}

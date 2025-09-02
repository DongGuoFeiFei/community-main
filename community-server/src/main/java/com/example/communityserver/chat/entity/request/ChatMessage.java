package com.example.communityserver.chat.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long sessionId;
    private MessageType type;
    private String content;
    private String sender;
    private String roomId;
    private Long senderId;
    private LocalDateTime timestamp;
    private String receiver;
    private String messageId;

    public enum MessageType {
        JOIN, LEAVE, CHAT, TYPING, SYSTEM, READ_RECEIPT,TYPING_START,TYPING_STOP
    }
}

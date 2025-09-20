package com.example.communityserver.chat.entity.request;

import com.example.communityserver.chat.entity.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private Long sessionId;
    private MessageType contentType;
    private String content;
    private Long senderId;
    private Date createTime;

}

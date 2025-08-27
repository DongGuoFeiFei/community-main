package com.example.communityserver.chat.entity.request;

import lombok.Data;
import java.util.Date;
/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/




@Data
public class ChatMessage {
    private String type;
    private Long senderId;
    private String content;
    private Date timestamp;

    // 基础构造方法
    public ChatMessage() {
        this.timestamp = new Date();
    }
}

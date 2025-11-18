package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ChatSession;
import com.example.communityserver.chat.mapper.ChatSessionMapper;
import com.example.communityserver.chat.service.ChatSessionService;
import org.springframework.stereotype.Service;

/**
 * 聊天会话 Service 实现
 */
@Service
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession>
        implements ChatSessionService {
}



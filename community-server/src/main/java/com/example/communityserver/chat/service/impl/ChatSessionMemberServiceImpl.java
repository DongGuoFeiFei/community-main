package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ChatSessionMember;
import com.example.communityserver.chat.mapper.ChatSessionMemberMapper;
import com.example.communityserver.chat.service.ChatSessionMemberService;
import org.springframework.stereotype.Service;

/**
 * 会话成员 Service 实现
 */
@Service
public class ChatSessionMemberServiceImpl extends ServiceImpl<ChatSessionMemberMapper, ChatSessionMember>
        implements ChatSessionMemberService {
}



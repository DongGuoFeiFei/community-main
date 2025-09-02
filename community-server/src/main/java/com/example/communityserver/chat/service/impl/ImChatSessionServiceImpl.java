package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ImChatSession;
import com.example.communityserver.chat.entity.model.ImMessage;
import com.example.communityserver.chat.entity.model.ImSessionMember;
import com.example.communityserver.chat.mapper.ImChatSessionMapper;
import com.example.communityserver.chat.mapper.ImMessageMapper;
import com.example.communityserver.chat.mapper.ImSessionMemberMapper;
import com.example.communityserver.chat.service.IImChatSessionService;
import com.example.communityserver.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-26
 **/

@Service
public class ImChatSessionServiceImpl extends ServiceImpl<ImChatSessionMapper, ImChatSession> implements IImChatSessionService {

    @Autowired
    private ImChatSessionMapper chatSessionMapper;

    @Autowired
    private ImSessionMemberMapper sessionMemberMapper;

    @Autowired
    private ImMessageMapper messageMapper;

    @Override
    public List<ImChatSession> getSessions() {
        List<ImSessionMember> imSessionMembers = sessionMemberMapper.selectList(new LambdaQueryWrapper<ImSessionMember>().eq(ImSessionMember::getUserId, SecurityUtils.getLoginUserId()));
        List<Long> sessionIds = imSessionMembers.stream().map(ImSessionMember::getSessionId).collect(Collectors.toList());
        List<ImChatSession> imChatSessions = chatSessionMapper.selectList(new LambdaQueryWrapper<ImChatSession>()
                .in(!sessionIds.isEmpty(), ImChatSession::getId, sessionIds));
        return imChatSessions;
    }

    @Override
    public List<ImMessage> getSessionMessages(Long sessionId) {
        List<ImMessage> imMessages = messageMapper.selectList(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getSessionId, sessionId));
        return imMessages;
    }
}

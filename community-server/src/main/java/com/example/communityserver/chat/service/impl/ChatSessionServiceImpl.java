package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.enums.MemberRole;
import com.example.communityserver.chat.entity.enums.SessionType;
import com.example.communityserver.chat.entity.model.ChatSession;
import com.example.communityserver.chat.entity.model.ChatSessionMember;
import com.example.communityserver.chat.entity.response.SessionDetailResponse;
import com.example.communityserver.chat.entity.response.SessionListItemResponse;
import com.example.communityserver.chat.mapper.ChatSessionMapper;
import com.example.communityserver.chat.service.ChatSessionMemberService;
import com.example.communityserver.chat.service.ChatSessionService;
import com.example.communityserver.entity.model.User;

import com.example.communityserver.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会话服务实现
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession>
        implements ChatSessionService {

    private final ChatSessionMemberService memberService;
    private final IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrGetPrivateSession(Long userId1, Long userId2) {
        // 1. 参数校验
        if (userId1 == null || userId2 == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("不能与自己创建会话");
        }

        // 2. 查询是否已存在私聊会话
        // 查询userId1的所有私聊会话
        List<ChatSessionMember> user1Members = memberService.lambdaQuery()
                .eq(ChatSessionMember::getUserId, userId1)
                .eq(ChatSessionMember::getStatus, 1)
                .list();

        for (ChatSessionMember member : user1Members) {
            ChatSession session = this.getById(member.getSessionId());
            if (session != null && SessionType.PRIVATE.getCode().equals(session.getType())) {
                // 检查这个会话是否包含userId2
                ChatSessionMember user2Member = memberService.lambdaQuery()
                        .eq(ChatSessionMember::getSessionId, session.getId())
                        .eq(ChatSessionMember::getUserId, userId2)
                        .eq(ChatSessionMember::getStatus, 1)
                        .one();

                if (user2Member != null) {
                    log.info("找到已存在的私聊会话: {}", session.getId());
                    return session.getId();
                }
            }
        }

        // 3. 不存在则创建新会话
        ChatSession session = new ChatSession();
        session.setType(SessionType.PRIVATE.getCode());
        session.setStatus(1);
        this.save(session);

        // 4. 添加两个成员
        memberService.addMember(session.getId(), userId1, MemberRole.MEMBER.getCode());
        memberService.addMember(session.getId(), userId2, MemberRole.MEMBER.getCode());

        log.info("创建新的私聊会话: {}, 成员: {} 和 {}", session.getId(), userId1, userId2);
        return session.getId();
    }

    @Override
    public List<SessionListItemResponse> getUserSessions(Long userId) {
        // 1. 查询用户的所有会话成员记录
        List<ChatSessionMember> members = memberService.lambdaQuery()
                .eq(ChatSessionMember::getUserId, userId)
                .eq(ChatSessionMember::getStatus, 1)
                .list();

        if (members.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 获取所有会话ID
        List<Long> sessionIds = members.stream()
                .map(ChatSessionMember::getSessionId)
                .collect(Collectors.toList());

        // 3. 查询会话信息
        List<ChatSession> sessions = this.lambdaQuery()
                .in(ChatSession::getId, sessionIds)
                .eq(ChatSession::getStatus, 1)
                .orderByDesc(ChatSession::getLastMsgTime)
                .list();

        // 4. 构建响应列表
        List<SessionListItemResponse> result = new ArrayList<>();
        for (ChatSession session : sessions) {
            SessionListItemResponse item = new SessionListItemResponse();
            item.setId(session.getId());
            item.setType(session.getType());
            item.setLastMsgContent(session.getLastMsgContent());
            item.setLastMsgTime(session.getLastMsgTime());

            // 获取当前用户的成员信息
            ChatSessionMember member = members.stream()
                    .filter(m -> m.getSessionId().equals(session.getId()))
                    .findFirst()
                    .orElse(null);

            if (member != null) {
                item.setIsPinned(member.getIsPinned() != null && member.getIsPinned() == 1);
                item.setIsMuted(member.getIsMuted() != null && member.getIsMuted() == 1);

                // 计算未读数
                Integer unreadCount = memberService.getUnreadCount(
                        session.getId(),
                        userId,
                        session.getLastMsgId()
                );
                item.setUnreadCount(unreadCount);
            }

            // 如果是私聊，获取对方信息
            if (SessionType.PRIVATE.getCode().equals(session.getType())) {
                ChatSessionMember peerMember = memberService.lambdaQuery()
                        .eq(ChatSessionMember::getSessionId, session.getId())
                        .ne(ChatSessionMember::getUserId, userId)
                        .eq(ChatSessionMember::getStatus, 1)
                        .one();

                if (peerMember != null) {
                    User peerUser = userService.getById(peerMember.getUserId());
                    if (peerUser != null) {
                        item.setName(peerUser.getNickname() != null ? peerUser.getNickname() : peerUser.getUsername());
                        item.setAvatar(peerUser.getAvatar());
                        // TODO: 设置在线状态
                        item.setIsOnline(false);
                    }
                }
            } else {
                // 群聊使用会话的名称和头像
                item.setName(session.getName());
                item.setAvatar(session.getAvatar());
            }

            result.add(item);
        }

        return result;
    }

    @Override
    public SessionDetailResponse getSessionDetail(Long sessionId, Long currentUserId) {
        // 1. 查询会话信息
        ChatSession session = this.getById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("会话不存在");
        }

        // 2. 验证用户是否是成员
        if (!isMember(sessionId, currentUserId)) {
            throw new IllegalArgumentException("您不是该会话的成员");
        }

        // 3. 构建响应
        SessionDetailResponse response = new SessionDetailResponse();
        response.setId(session.getId());
        response.setType(session.getType());
        response.setName(session.getName());
        response.setAvatar(session.getAvatar());
        response.setCreatedAt(session.getCreatedAt());

        // 4. 查询成员列表
        List<ChatSessionMember> members = memberService.lambdaQuery()
                .eq(ChatSessionMember::getSessionId, sessionId)
                .eq(ChatSessionMember::getStatus, 1)
                .list();

        List<SessionDetailResponse.MemberInfo> memberInfos = new ArrayList<>();
        for (ChatSessionMember member : members) {
            User user = userService.getById(member.getUserId());
            if (user != null) {
                SessionDetailResponse.MemberInfo memberInfo = new SessionDetailResponse.MemberInfo();
                memberInfo.setUserId(user.getUserId());
                memberInfo.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername());
                memberInfo.setAvatar(user.getAvatar());
                memberInfo.setRole(member.getRole());
                memberInfo.setJoinedAt(member.getJoinedAt());
                memberInfos.add(memberInfo);
            }
        }
        response.setMembers(memberInfos);

        // 5. 如果是私聊，设置对方信息
        if (SessionType.PRIVATE.getCode().equals(session.getType())) {
            ChatSessionMember peerMember = members.stream()
                    .filter(m -> !m.getUserId().equals(currentUserId))
                    .findFirst()
                    .orElse(null);

            if (peerMember != null) {
                User peerUser = userService.getById(peerMember.getUserId());
                if (peerUser != null) {
                    SessionDetailResponse.PeerUserInfo peerInfo = new SessionDetailResponse.PeerUserInfo();
                    peerInfo.setUserId(peerUser.getUserId());
                    peerInfo.setNickname(peerUser.getNickname() != null ? peerUser.getNickname() : peerUser.getUsername());
                    peerInfo.setAvatar(peerUser.getAvatar());
                    peerInfo.setSignature(userService.getUserProfile(peerUser.getUserId()).getSignature());
                    // TODO: 设置在线状态
                    peerInfo.setIsOnline(false);
                    response.setPeer(peerInfo);
                }
            }
        }

        return response; 
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLastMessage(Long sessionId, Long messageId, String content,
                                   Long senderId, LocalDateTime sendTime) {
        ChatSession session = this.getById(sessionId);
        if (session == null) {
            log.warn("会话不存在: {}", sessionId);
            return;
        }

        session.setLastMsgId(messageId);
        session.setLastMsgContent(content);
        session.setLastMsgSenderId(senderId);
        session.setLastMsgTime(sendTime);

        this.updateById(session);
        log.debug("更新会话 {} 的最新消息: {}", sessionId, messageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSession(Long sessionId, Long userId) {
        // 验证用户是否是成员
        ChatSessionMember member = memberService.getMember(sessionId, userId);
        if (member == null) {
            throw new IllegalArgumentException("您不是该会话的成员");
        }

        // 更新成员状态为已退出
        member.setStatus(2);
        boolean success = memberService.updateById(member);

        if (success) {
            log.info("用户 {} 删除了会话: {}", userId, sessionId);
        }

        return success;
    }

    @Override
    public boolean isMember(Long sessionId, Long userId) {
        ChatSessionMember member = memberService.getMember(sessionId, userId);
        return member != null && member.getStatus() == 1;
    }
}

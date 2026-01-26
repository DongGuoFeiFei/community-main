package com.example.communityserver.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.chat.entity.model.ChatSessionMember;
import com.example.communityserver.chat.mapper.ChatSessionMemberMapper;
import com.example.communityserver.chat.service.ChatSessionMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 会话成员服务实现
 *
 * @author DongGuo
 * @since 2026-01-23
 */
@Slf4j
@Service
public class ChatSessionMemberServiceImpl extends ServiceImpl<ChatSessionMemberMapper, ChatSessionMember>
        implements ChatSessionMemberService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMember(Long sessionId, Long userId, Integer role) {
        // 检查是否已存在
        ChatSessionMember existMember = this.lambdaQuery()
                .eq(ChatSessionMember::getSessionId, sessionId)
                .eq(ChatSessionMember::getUserId, userId)
                .one();

        if (existMember != null) {
            // 如果已存在但状态是已退出，则重新激活
            if (existMember.getStatus() == 2) {
                existMember.setStatus(1);
                existMember.setJoinedAt(LocalDateTime.now());
                return this.updateById(existMember);
            }
            log.warn("成员已存在: sessionId={}, userId={}", sessionId, userId);
            return true;
        }

        // 创建新成员
        ChatSessionMember member = new ChatSessionMember();
        member.setSessionId(sessionId);
        member.setUserId(userId);
        member.setRole(role);
        member.setStatus(1);
        member.setIsMuted(0);
        member.setIsPinned(0);
        member.setJoinedAt(LocalDateTime.now());

        boolean success = this.save(member);
        if (success) {
            log.info("添加成员成功: sessionId={}, userId={}, role={}", sessionId, userId, role);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markAsRead(Long sessionId, Long userId, Long messageId) {
        ChatSessionMember member = getMember(sessionId, userId);
        if (member == null) {
            log.warn("成员不存在: sessionId={}, userId={}", sessionId, userId);
            return false;
        }

        // 只有当新的messageId大于当前已读ID时才更新
        if (member.getLastReadMsgId() == null || messageId > member.getLastReadMsgId()) {
            member.setLastReadMsgId(messageId);
            member.setLastReadTime(LocalDateTime.now());
            boolean success = this.updateById(member);

            if (success) {
                log.debug("标记已读成功: sessionId={}, userId={}, messageId={}", sessionId, userId, messageId);
            }
            return success;
        }

        return true;
    }

    @Override
    public Integer getUnreadCount(Long sessionId, Long userId, Long lastMsgId) {
        if (lastMsgId == null) {
            return 0;
        }

        ChatSessionMember member = getMember(sessionId, userId);
        if (member == null) {
            return 0;
        }

        Long lastReadMsgId = member.getLastReadMsgId();
        if (lastReadMsgId == null) {
            // 如果从未读过，则所有消息都是未读的
            // 这里简化处理，返回一个标识值
            return 99;
        }

        // 计算未读数：最新消息ID - 已读消息ID
        // 注意：这是一个简化的计算方式，假设消息ID是递增的
        long unread = lastMsgId - lastReadMsgId;
        return unread > 0 ? (int) Math.min(unread, 99) : 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPinned(Long sessionId, Long userId, boolean isPinned) {
        ChatSessionMember member = getMember(sessionId, userId);
        if (member == null) {
            log.warn("成员不存在: sessionId={}, userId={}", sessionId, userId);
            return false;
        }

        member.setIsPinned(isPinned ? 1 : 0);
        boolean success = this.updateById(member);

        if (success) {
            log.info("设置置顶: sessionId={}, userId={}, isPinned={}", sessionId, userId, isPinned);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setMuted(Long sessionId, Long userId, boolean isMuted) {
        ChatSessionMember member = getMember(sessionId, userId);
        if (member == null) {
            log.warn("成员不存在: sessionId={}, userId={}", sessionId, userId);
            return false;
        }

        member.setIsMuted(isMuted ? 1 : 0);
        boolean success = this.updateById(member);

        if (success) {
            log.info("设置免打扰: sessionId={}, userId={}, isMuted={}", sessionId, userId, isMuted);
        }
        return success;
    }

    @Override
    public ChatSessionMember getMember(Long sessionId, Long userId) {
        return this.lambdaQuery()
                .eq(ChatSessionMember::getSessionId, sessionId)
                .eq(ChatSessionMember::getUserId, userId)
                .one();
    }
}

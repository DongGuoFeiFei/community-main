package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ChatSession;
import com.example.communityserver.chat.entity.response.SessionDetailResponse;
import com.example.communityserver.chat.entity.response.SessionListItemResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会话服务接口
 *
 * @author DongGuo
 * @since 2026-01-23
 */
public interface ChatSessionService extends IService<ChatSession> {

    /**
     * 创建或获取私聊会话
     *
     * @param userId1 用户1
     * @param userId2 用户2
     * @return 会话ID
     */
    Long createOrGetPrivateSession(Long userId1, Long userId2);

    /**
     * 获取用户的会话列表（带未读数）
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<SessionListItemResponse> getUserSessions(Long userId);

    /**
     * 获取会话详情
     *
     * @param sessionId     会话ID
     * @param currentUserId 当前用户ID
     * @return 会话详情
     */
    SessionDetailResponse getSessionDetail(Long sessionId, Long currentUserId);

    /**
     * 更新会话最新消息
     *
     * @param sessionId 会话ID
     * @param messageId 消息ID
     * @param content   消息内容摘要
     * @param senderId  发送者ID
     * @param sendTime  发送时间
     */
    void updateLastMessage(Long sessionId, Long messageId, String content,
                           Long senderId, LocalDateTime sendTime);

    /**
     * 删除会话（软删除）
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @return 是否成功
     */
    boolean deleteSession(Long sessionId, Long userId);

    /**
     * 检查用户是否是会话成员
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @return 是否是成员
     */
    boolean isMember(Long sessionId, Long userId);
}

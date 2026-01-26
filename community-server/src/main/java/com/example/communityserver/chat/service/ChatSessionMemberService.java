package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ChatSessionMember;

/**
 * 会话成员服务接口
 *
 * @author DongGuo
 * @since 2026-01-23
 */
public interface ChatSessionMemberService extends IService<ChatSessionMember> {

    /**
     * 添加成员到会话
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param role      角色
     * @return 是否成功
     */
    boolean addMember(Long sessionId, Long userId, Integer role);

    /**
     * 标记消息已读
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param messageId 消息ID
     * @return 是否成功
     */
    boolean markAsRead(Long sessionId, Long userId, Long messageId);

    /**
     * 获取未读消息数
     *
     * @param sessionId     会话ID
     * @param userId        用户ID
     * @param lastMsgId     会话最新消息ID
     * @return 未读数
     */
    Integer getUnreadCount(Long sessionId, Long userId, Long lastMsgId);

    /**
     * 设置置顶
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param isPinned  是否置顶
     * @return 是否成功
     */
    boolean setPinned(Long sessionId, Long userId, boolean isPinned);

    /**
     * 设置免打扰
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param isMuted   是否免打扰
     * @return 是否成功
     */
    boolean setMuted(Long sessionId, Long userId, boolean isMuted);

    /**
     * 获取用户在会话中的成员信息
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @return 成员信息
     */
    ChatSessionMember getMember(Long sessionId, Long userId);
}

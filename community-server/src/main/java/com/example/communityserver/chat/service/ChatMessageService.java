package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.request.SendMessageRequest;
import com.example.communityserver.chat.entity.response.MessagePageResponse;

/**
 * 消息服务接口
 *
 * @author DongGuo
 * @since 2026-01-23
 */
public interface ChatMessageService extends IService<ChatMessage> {

    /**
     * 发送消息
     *
     * @param sessionId 会话ID
     * @param senderId  发送者ID
     * @param request   消息请求
     * @return 消息实体
     */
    ChatMessage sendMessage(Long sessionId, Long senderId, SendMessageRequest request);

    /**
     * 分页获取会话消息
     *
     * @param sessionId 会话ID
     * @param lastMsgId 上一页最后一条消息ID（首次传null）
     * @param pageSize  每页大小
     * @return 消息分页
     */
    MessagePageResponse getMessages(Long sessionId, Long lastMsgId, Integer pageSize);

    /**
     * 撤回消息
     *
     * @param messageId 消息ID
     * @param userId    用户ID
     * @return 是否成功
     */
    boolean recallMessage(Long messageId, Long userId);

    /**
     * 获取消息内容摘要（用于会话列表显示）
     *
     * @param msgType 消息类型
     * @param content 消息内容
     * @return 摘要文本
     */
    String getMessageDigest(Integer msgType, String content);
}

package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ChatMessage;
import com.example.communityserver.chat.entity.response.MessagePageResponse;

/**
 * 聊天消息 Service 接口
 */
public interface ChatMessageService extends IService<ChatMessage> {

    /**
     * 异步保存聊天消息
     *
     * @param message 消息实体
     */
    void asyncSaveMessage(ChatMessage message);

    /**
     * 分页获取会话消息
     *
     * @param sessionId 会话ID
     * @param lastSeq   上一次已读/已拉取的序列号
     * @param pageSize  单次拉取数量
     * @return 消息分页响应
     */
    MessagePageResponse getMessagePage(Long sessionId, Long lastSeq, Integer pageSize);

    /**
     * 根据会话和序列号查找消息
     *
     * @param sessionId 会话ID
     * @param msgSeq    序列号
     * @return 消息实体
     */
    ChatMessage findBySessionAndSeq(Long sessionId, Long msgSeq);
}

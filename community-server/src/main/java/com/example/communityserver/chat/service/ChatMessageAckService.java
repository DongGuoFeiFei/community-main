package com.example.communityserver.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.chat.entity.model.ChatMessageAck;

/**
 * 消息已读状态 Service 接口
 */
public interface ChatMessageAckService extends IService<ChatMessageAck> {

    /**
     * 标记某个会话的消息为已读
     *
     * @param sessionId 会话ID
     * @param userId    用户ID
     * @param readSeq   已读的最大序列号
     * @return 是否成功
     */
    boolean markMessagesAsRead(Long sessionId, Long userId, Long readSeq);
}

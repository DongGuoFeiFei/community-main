import request from '@/utils/request';

/**
 * 获取会话消息历史
 * @param {number} sessionId 会话ID
 * @param {number} [lastMessageId] 最后一条消息ID，用于分页
 */
export const getMessages = (sessionId, lastMessageId) => {
    return request.get(`/chat/messages`, {
        params: { sessionId, lastMessageId }
    });
};

/**
 * 标记消息为已读
 * @param {number} sessionId 会话ID
 * @param {number} messageId 消息ID
 */
export const markMessageAsRead = (sessionId, messageId) => {
    return request.post(`/chat/messages/read`, { sessionId, messageId });
};

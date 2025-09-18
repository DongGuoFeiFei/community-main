import request from '@/utils/request';

/**
 * 获取用户会话列表
 */
export const getSessions = () => {
    return request.get('/chat/sessions');
};

/**
 * 创建私聊会话
 * @param {number} userId 对方用户ID
 */
export const createPrivateSession = (userId) => {
    return request.post('/chat/sessions/private', { userId });
};

/**
 * 获取会话详情
 * @param {number} sessionId 会话ID
 */
export const getSessionDetail = (sessionId) => {
    return request.get(`/chat/sessions/${sessionId}`);
};

/**
 * 删除会话
 * @param {number} sessionId 会话ID
 */
export const deleteSession = (sessionId) => {
    return request.delete(`/chat/sessions/${sessionId}`);
};

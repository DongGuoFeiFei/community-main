import request from '@/utils/request'

// 获取会话列表
export const getSessions = () => {
    return request.get('/chat/sessions')
}

// 获取会话消息
export const getSessionMessages = (sessionId, lastMessageId = null) => {
    const params = {}
    if (lastMessageId) {
        params.lastMessageId = lastMessageId
    }
    return request.get(`/chat/sessions/${sessionId}/messages`, { params })
}

// 创建会话
export const createSession = (userId) => {
    return request.post('/chat/sessions', { userId })
}

// 发送消息
export const sendMessage = (sessionId, content, type = 1) => {
    return request.post('/chat/messages', {
        sessionId,
        content,
        contentType: type
    })
}

// 标记消息已读
export const markMessageRead = (sessionId, messageId) => {
    return request.post('/chat/messages/read', {
        sessionId,
        messageId
    })
}

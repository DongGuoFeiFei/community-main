import request from '@/utils/request'

// 获取会话列表
export const getSessions = () => {
    return request.get('/chat/sessions')
}

// 获取会话消息
export const getSessionMessages = (sessionId, params = {}) => {
    return request.get(`/chat/sessions/${sessionId}/messages`, { params })
}

// 发送文本消息
export const sendTextMessage = (sessionId, content) => {
    return request.post('/chat/messages/text', {
        sessionId,
        content
    })
}

// 标记消息已读
export const markMessagesAsRead = (sessionId, messageIds) => {
    return request.post('/chat/messages/read', {
        sessionId,
        messageIds
    })
}

// 创建群聊
export const createGroup = (data) => {
    return request.post('/chat/groups', data)
}

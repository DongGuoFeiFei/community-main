import request from '@/utils/request';
import type { ApiResponse } from '@/types/http';
import type { ChatSessionDetail, ChatSessionItem } from '@/types/chat';

/**
 * 获取会话列表
 */
export const getSessions = () => {
  return request.get<ApiResponse<ChatSessionItem[]>>('/chat/sessions');
};

/**
 * 创建私聊会话
 */
export const createPrivateSession = (userId: number) => {
  return request.post<ApiResponse<ChatSessionDetail>>('/chat/sessions/private', { userId });
};

/**
 * 获取会话详情
 */
export const getSessionDetail = (sessionId: number) => {
  return request.get<ApiResponse<ChatSessionDetail>>(`/chat/sessions/${sessionId}`);
};

/**
 * 删除会话
 */
export const deleteSession = (sessionId: number) => {
  return request.delete<ApiResponse<boolean>>(`/chat/sessions/${sessionId}`);
};


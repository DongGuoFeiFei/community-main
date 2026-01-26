/**
 * 消息相关 API
 *
 * @author DongGuo
 * @since 2025-01-23
 */

import request from "@/utils/request";
import type { ApiResponse } from "@/types/http";
import type {
  ChatMessage,
  MessagePageResponse,
  SendMessageRequest,
  MarkReadRequest,
} from "@/types/chat";

/**
 * 获取消息列表
 * @param sessionId 会话ID
 * @param lastMsgId 上一页最后一条消息ID
 * @param pageSize 每页大小
 */
export const getMessages = (
  sessionId: number,
  lastMsgId?: number | null,
  pageSize?: number,
) => {
  return request.get<ApiResponse<MessagePageResponse>>(
    `/chat/sessions/${sessionId}/messages`,
    {
      params: {
        lastMsgId,
        pageSize: pageSize || 20,
      },
    },
  );
};

/**
 * 发送消息
 * @param sessionId 会话ID
 * @param data 消息数据
 */
export const sendMessage = (sessionId: number, data: SendMessageRequest) => {
  return request.post<ApiResponse<ChatMessage>>(
    `/chat/sessions/${sessionId}/messages`,
    data,
  );
};

/**
 * 撤回消息
 * @param sessionId 会话ID
 * @param messageId 消息ID
 */
export const recallMessage = (sessionId: number, messageId: number) => {
  return request.delete<ApiResponse<boolean>>(
    `/chat/sessions/${sessionId}/messages/${messageId}`,
  );
};

/**
 * 标记消息已读
 * @param sessionId 会话ID
 * @param data 已读数据
 */
export const markMessageAsRead = (sessionId: number, data: MarkReadRequest) => {
  return request.post<ApiResponse<boolean>>(
    `/chat/sessions/${sessionId}/messages/read`,
    data,
  );
};

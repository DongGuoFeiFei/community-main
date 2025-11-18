import request from "@/utils/request";
import type { ApiResponse } from "@/types/http";
import type { ChatMessage, MessagePageResponse } from "@/types/chat";

/**
 * 获取消息列表
 * @param sessionId 会话ID
 * @param lastSeq 最后一条消息的seq
 */
export const getMessages = (sessionId: number, lastSeq?: number | null) => {
  return request.get<ApiResponse<MessagePageResponse | ChatMessage[]>>(
    "/chat/messages",
    {
      params: {
        sessionId,
        lastSeq,
      },
    }
  );
};

/**
 * 标记消息已读
 * @param sessionId 会话ID
 * @param readSeq 已读的seq
 */
export const markMessageAsRead = (sessionId: number, readSeq: number) => {
  return request.post<ApiResponse<boolean>>("/chat/messages/read", {
    sessionId,
    readSeq,
  });
};

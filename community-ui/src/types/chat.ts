/**
 * 聊天相关类型定义
 *
 * @author DongGuo
 * @since 2025-01-23
 */

/**
 * 会话类型枚举
 */
export enum SessionType {
  /** 私聊 */
  PRIVATE = 1,
  /** 群聊 */
  GROUP = 2,
}

/**
 * 消息类型枚举
 */
export enum MessageType {
  /** 文本消息 */
  TEXT = 1,
  /** 图片消息 */
  IMAGE = 2,
  /** 文件消息 */
  FILE = 3,
  /** 系统消息 */
  SYSTEM = 4,
}

/**
 * 消息状态枚举
 */
export enum MessageStatus {
  /** 正常 */
  NORMAL = 1,
  /** 已撤回 */
  RECALLED = 2,
  /** 已删除 */
  DELETED = 3,
}

/**
 * 成员角色枚举
 */
export enum MemberRole {
  /** 普通成员 */
  MEMBER = 1,
  /** 管理员 */
  ADMIN = 2,
  /** 群主 */
  OWNER = 3,
}

/**
 * 会话列表项
 */
export interface ChatSessionItem {
  /** 会话ID */
  id: number;
  /** 会话类型：1-私聊 2-群聊 */
  type: number;
  /** 会话名称 */
  name: string;
  /** 会话头像 */
  avatar?: string;
  /** 最新消息内容 */
  lastMsgContent?: string;
  /** 最新消息时间 */
  lastMsgTime?: string;
  /** 未读消息数 */
  unreadCount: number;
  /** 是否置顶 */
  isPinned: boolean;
  /** 是否免打扰 */
  isMuted: boolean;
  /** 对方在线状态（私聊专用） */
  isOnline?: boolean;
}

/**
 * 成员信息
 */
export interface MemberInfo {
  /** 用户ID */
  userId: number;
  /** 昵称 */
  nickname: string;
  /** 头像 */
  avatar?: string;
  /** 角色：1-普通 2-管理员 3-群主 */
  role: number;
  /** 加入时间 */
  joinedAt: string;
}

/**
 * 对方用户信息（私聊）
 */
export interface PeerUserInfo {
  /** 用户ID */
  userId: number;
  /** 昵称 */
  nickname: string;
  /** 头像 */
  avatar?: string;
  /** 个性签名 */
  signature?: string;
  /** 是否在线 */
  isOnline: boolean;
}

/**
 * 会话详情
 */
export interface ChatSessionDetail {
  /** 会话ID */
  id: number;
  /** 会话类型：1-私聊 2-群聊 */
  type: number;
  /** 会话名称 */
  name?: string;
  /** 会话头像 */
  avatar?: string;
  /** 创建时间 */
  createdAt: string;
  /** 成员列表 */
  members?: MemberInfo[];
  /** 对方用户信息（私聊专用） */
  peer?: PeerUserInfo;
}

/**
 * 聊天消息
 */
export interface ChatMessage {
  /** 消息ID */
  id: number;
  /** 会话ID */
  sessionId: number;
  /** 发送者ID */
  senderId: number;
  /** 发送者昵称 */
  senderName: string;
  /** 发送者头像 */
  senderAvatar?: string;
  /** 消息类型：1-文本 2-图片 3-文件 4-系统 */
  msgType: number;
  /** 消息内容 */
  content: string;
  /** 扩展数据 */
  extraData?: string | Record<string, any>;
  /** 引用的消息ID */
  quoteMsgId?: number;
  /** 状态：1-正常 2-已撤回 3-已删除 */
  status: number;
  /** 发送时间 */
  sendTime: string;
  /** 创建时间 */
  createdAt?: string;
}

/**
 * 消息分页响应
 */
export interface MessagePageResponse {
  /** 消息列表 */
  messages: ChatMessage[];
  /** 是否还有更多 */
  hasMore: boolean;
  /** 下一页的游标（最后一条消息的ID） */
  nextCursor?: number | null;
}

/**
 * 创建私聊会话请求
 */
export interface CreatePrivateSessionRequest {
  /** 对方用户ID */
  userId: number;
}

/**
 * 发送消息请求
 */
export interface SendMessageRequest {
  /** 消息类型：1-文本 2-图片 */
  msgType: number;
  /** 消息内容 */
  content: string;
  /** 引用的消息ID */
  quoteMsgId?: number;
  /** 扩展数据（JSON字符串） */
  extraData?: string;
}

/**
 * 标记已读请求
 */
export interface MarkReadRequest {
  /** 已读到的消息ID */
  messageId: number;
}

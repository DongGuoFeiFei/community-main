export interface ChatSessionItem {
  id: number;
  type: number;
  bizType?: number;
  name: string;
  avatar?: string;
  ownerId?: number;
  lastMsgId?: number;
  lastMsgSeq?: number;
  lastMsgDigest?: string;
  lastMsgUserId?: number;
  lastMsgTime?: string;
  memberCount?: number;
  unreadCount?: number;
  status?: number;
  isOnline?: boolean;
  ext?: Record<string, any>;
}

export interface PeerUser {
  userId: number;
  nickname: string;
  avatar?: string;
  signature?: string;
}

export interface ChatMember {
  userId: number;
  nickname: string;
  role: number;
  avatar?: string;
  joinStatus?: number;
  muteUntil?: string;
  unreadSeq?: number;
  readSeq?: number;
}

export interface ChatSessionDetail {
  session: ChatSessionItem;
  peer?: PeerUser;
  members?: ChatMember[];
}

export interface ChatMessage {
  id: number;
  sessionId: number;
  msgSeq?: number;
  senderId: number;
  senderName: string;
  senderAvatar?: string;
  contentType: number;
  content: string;
  contentJson?: Record<string, any> | string | null;
  atUserIds?: number[] | string | null;
  quoteMsgId?: number | null;
  fileSize?: number | null;
  status?: number;
  sendTime?: string;
  createdAt?: string;
}

export interface MessagePageResponse {
  records: ChatMessage[];
  hasMore: boolean;
  nextSeq: number | null;
  cursor?: number | null;
  prevSeq?: number | null;
}


/**
 * 全局类型定义文件
 * 新模块使用 TypeScript 时从这里导入类型
 */

// ============= 用户相关 =============
export interface User {
  id: string
  username: string
  email: string
  avatar?: string
  nickname?: string
  signature?: string
  phone?: string
  gender?: 0 | 1 | 2  // 0:未知 1:男 2:女
  birthday?: string
  lastLogin: string
  createTime: string
  points?: number
  level?: number
}

export interface UserInfo {
  userInfo: User
  expiresIn: number
}

// ============= 文章相关 =============
export interface Article {
  id: string
  title: string
  content: string
  summary?: string
  coverImage?: string
  author: User
  authorId: string
  categoryId?: string
  category?: Category
  tags?: Tag[]
  viewCount: number
  likeCount: number
  collectCount: number
  commentCount: number
  status: 0 | 1 | 2  // 0:草稿 1:已发布 2:已删除
  isTop?: boolean
  createTime: string
  updateTime: string
}

export interface ArticleListParams {
  page: number
  pageSize: number
  categoryId?: string
  authorId?: string
  keyword?: string
  tagId?: string
  sortBy?: 'createTime' | 'viewCount' | 'likeCount'
  order?: 'asc' | 'desc'
}

// ============= 分类和标签 =============
export interface Category {
  id: string
  name: string
  description?: string
  icon?: string
  sort?: number
  parentId?: string
  children?: Category[]
}

export interface Tag {
  id: string
  name: string
  color?: string
  articleCount?: number
}

// ============= 评论相关 =============
export interface Comment {
  id: string
  content: string
  articleId: string
  userId: string
  user: User
  parentId?: string
  replyTo?: User
  likeCount: number
  children?: Comment[]
  createTime: string
}

// ============= 聊天相关 =============
export interface ChatSession {
  id: string
  type: 'private' | 'group'
  name?: string
  avatar?: string
  participants: User[]
  lastMessage?: ChatMessage
  unreadCount: number
  createTime: string
  updateTime: string
}

export interface ChatMessage {
  id: string
  sessionId: string
  senderId: string
  sender: User
  content: string
  type: 'text' | 'image' | 'file' | 'emoji'
  status: 'sending' | 'sent' | 'failed'
  createTime: string
}

// ============= 通知相关 =============
export interface Notification {
  id: string
  type: 'like' | 'comment' | 'follow' | 'system' | 'reply'
  title: string
  content: string
  fromUser?: User
  targetId?: string  // 关联的文章/评论等 ID
  isRead: boolean
  createTime: string
}

// ============= 关注相关 =============
export interface Follow {
  id: string
  followerId: string
  follower: User
  followingId: string
  following: User
  createTime: string
}

// ============= 积分相关 =============
export interface PointRecord {
  id: string
  userId: string
  type: 'earn' | 'spend'
  amount: number
  reason: string
  balance: number
  createTime: string
}

// ============= 支付相关 =============
export interface PaymentOrder {
  id: string
  orderNo: string
  userId: string
  amount: number
  status: 'pending' | 'paid' | 'failed' | 'refunded'
  paymentMethod?: 'alipay' | 'wechat' | 'points'
  description: string
  createTime: string
  paidTime?: string
}

// ============= API 响应相关 =============
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
  timestamp?: number
}

export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}

// ============= 表单相关 =============
export interface LoginForm {
  username: string
  password: string
  remember?: boolean
}

export interface RegisterForm {
  username: string
  email: string
  password: string
  confirmPassword: string
  code?: string
}

export interface ArticleForm {
  title: string
  content: string
  summary?: string
  coverImage?: string
  categoryId?: string
  tags?: string[]
  status: 0 | 1
}

// ============= 组件 Props 相关 =============
export interface UserCardProps {
  user: User
  showActions?: boolean
  size?: 'small' | 'medium' | 'large'
}

export interface ArticleCardProps {
  article: Article
  mode?: 'list' | 'card' | 'simple'
  showAuthor?: boolean
  showStats?: boolean
}

// ============= WebSocket 相关 =============
export interface WebSocketMessage {
  type: 'chat' | 'notification' | 'online' | 'offline'
  data: any
  timestamp: number
}

// ============= 工具类型 =============
export type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P]
}

export type Nullable<T> = T | null

export type Optional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>

// ============= 常量类型 =============
export type UserGender = 0 | 1 | 2
export type ArticleStatus = 0 | 1 | 2
export type NotificationType = 'like' | 'comment' | 'follow' | 'system' | 'reply'
export type ChatSessionType = 'private' | 'group'
export type MessageType = 'text' | 'image' | 'file' | 'emoji'


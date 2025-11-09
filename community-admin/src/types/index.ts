/**
 * 通用类型定义
 * 供 TypeScript 文件使用
 */

// API 响应基础类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 分页参数
export interface PageParams {
  page: number
  pageSize: number
}

// 分页结果
export interface PageResult<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

// 用户信息（示例）
export interface UserInfo {
  id: number
  username: string
  email: string
  avatar?: string
  role: string
  status: 0 | 1
  createTime: string
  updateTime?: string
}

// 更多类型定义可以在这里添加...


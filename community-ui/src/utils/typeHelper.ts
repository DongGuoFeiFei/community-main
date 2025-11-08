/**
 * TypeScript 工具函数示例
 * 展示如何编写类型安全的工具函数
 */

import type { User, Article } from 'src/api/types'

/**
 * 安全地获取嵌套属性
 */
export const safeGet = <T, K extends keyof T>(
  obj: T | null | undefined,
  key: K,
  defaultValue?: T[K]
): T[K] | undefined => {
  return obj?.[key] ?? defaultValue
}

/**
 * 深度克隆对象（类型安全）
 */
export const deepClone = <T>(obj: T): T => {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * 数组去重（泛型函数）
 */
export const unique = <T>(arr: T[], key?: keyof T): T[] => {
  if (!key) {
    return Array.from(new Set(arr))
  }

  const seen = new Set()
  return arr.filter(item => {
    const value = item[key]
    if (seen.has(value)) {
      return false
    }
    seen.add(value)
    return true
  })
}

/**
 * 延迟函数（Promise 类型）
 */
export const delay = (ms: number): Promise<void> => {
  return new Promise(resolve => setTimeout(resolve, ms))
}

/**
 * 格式化用户名（类型约束）
 */
export const formatUsername = (user: User | null): string => {
  if (!user) return '游客'
  return user.nickname || user.username || '未知用户'
}

/**
 * 计算文章阅读时长（分钟）
 */
export const calculateReadTime = (article: Article): number => {
  const wordsPerMinute = 200
  const wordCount = article.content.length
  return Math.ceil(wordCount / wordsPerMinute)
}

/**
 * 类型守卫：检查是否为有效用户
 */
export const isValidUser = (user: any): user is User => {
  return (
    user &&
    typeof user.id === 'string' &&
    typeof user.username === 'string' &&
    typeof user.email === 'string'
  )
}

/**
 * 枚举转换为选项列表
 */
export const enumToOptions = <T extends Record<string, string | number>>(
  enumObj: T,
  labels: Record<keyof T, string>
): Array<{ label: string; value: T[keyof T] }> => {
  return Object.entries(enumObj).map(([key, value]) => ({
    label: labels[key as keyof T],
    value: value as T[keyof T]
  }))
}

/**
 * 安全的 JSON 解析
 */
export const safeJsonParse = <T = any>(
  jsonString: string,
  defaultValue: T
): T => {
  try {
    return JSON.parse(jsonString) as T
  } catch {
    return defaultValue
  }
}

/**
 * 防抖函数（带类型）
 */
export const debounce = <T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let timer: ReturnType<typeof setTimeout> | null = null

  return function (this: any, ...args: Parameters<T>) {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => {
      fn.apply(this, args)
    }, delay)
  }
}

/**
 * 节流函数（带类型）
 */
export const throttle = <T extends (...args: any[]) => any>(
  fn: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let lastTime = 0

  return function (this: any, ...args: Parameters<T>) {
    const now = Date.now()
    if (now - lastTime >= delay) {
      lastTime = now
      fn.apply(this, args)
    }
  }
}


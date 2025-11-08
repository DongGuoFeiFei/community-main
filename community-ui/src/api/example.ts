/**
 * 示例：新模块使用 TypeScript
 * 这个文件展示了如何在新功能中使用 TS
 */

import request from '@/utils/request'
import type { ApiResponse, Article, ArticleListParams, PageResult } from 'src/api/types'

// ============= 接口函数（带类型） =============

/**
 * 获取文章列表（TypeScript 版本）
 */
export const getArticleListTS = (params: ArticleListParams) => {
  return request.get<ApiResponse<PageResult<Article>>>('/api/articles', { params })
}

/**
 * 获取文章详情
 */
export const getArticleDetail = (id: string) => {
  return request.get<ApiResponse<Article>>(`/api/articles/${id}`)
}

/**
 * 创建文章
 */
export const createArticle = (data: Partial<Article>) => {
  return request.post<ApiResponse<Article>>('/api/articles', data)
}

/**
 * 更新文章
 */
export const updateArticle = (id: string, data: Partial<Article>) => {
  return request.put<ApiResponse<Article>>(`/api/articles/${id}`, data)
}

/**
 * 删除文章
 */
export const deleteArticle = (id: string) => {
  return request.delete<ApiResponse<void>>(`/api/articles/${id}`)
}

// ============= 可以在 TS 文件中导入 JS 模块 =============

// 导入现有的 JS 模块（完全兼容）
import { getMyArticles as getArticleListJS } from "./article";

// 混合使用 JS 和 TS 的函数
export const getArticles = async (useNewAPI: boolean, params: ArticleListParams) => {
  if (useNewAPI) {
    // 使用新的 TS 版本
    return getArticleListTS(params)
  } else {
    // 使用旧的 JS 版本
    return getArticleListJS(params)
  }
}


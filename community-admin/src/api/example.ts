/**
 * 示例：TypeScript API 文件
 * 这是一个演示如何在新功能中使用 TypeScript 的例子
 */

import request from '@/utils/request'
import type { ApiResponse, PageParams, PageResult } from '@/types'

// 定义文章接口
export interface Article {
  id: number
  title: string
  content: string
  author: string
  categoryId: number
  tags: string[]
  viewCount: number
  likeCount: number
  status: 'draft' | 'published' | 'archived'
  createTime: string
  updateTime?: string
}

// 定义文章创建/更新的数据接口
export interface ArticleFormData {
  title: string
  content: string
  categoryId: number
  tags: string[]
  status?: 'draft' | 'published'
}

/**
 * 获取文章列表
 * @param params 分页参数
 * @returns 文章列表
 */
export function getArticleList(
  params: PageParams
): Promise<ApiResponse<PageResult<Article>>> {
  return request({
    url: '/article/list',
    method: 'get',
    params
  })
}

/**
 * 获取文章详情
 * @param id 文章ID
 * @returns 文章详情
 */
export function getArticleDetail(id: number): Promise<ApiResponse<Article>> {
  return request({
    url: `/article/${id}`,
    method: 'get'
  })
}

/**
 * 创建文章
 * @param data 文章数据
 * @returns 创建结果
 */
export function createArticle(
  data: ArticleFormData
): Promise<ApiResponse<Article>> {
  return request({
    url: '/article/create',
    method: 'post',
    data
  })
}

/**
 * 更新文章
 * @param id 文章ID
 * @param data 文章数据
 * @returns 更新结果
 */
export function updateArticle(
  id: number,
  data: Partial<ArticleFormData>
): Promise<ApiResponse<void>> {
  return request({
    url: `/article/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除文章
 * @param id 文章ID
 * @returns 删除结果
 */
export function deleteArticle(id: number): Promise<ApiResponse<void>> {
  return request({
    url: `/article/${id}`,
    method: 'delete'
  })
}


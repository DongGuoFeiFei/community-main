import request from '@/utils/request'

/**
 * 获取所有分类
 */
export const getCategories = () => {
    return request.get('/category/list')
}


/**
 * 获取所有分类树
 */
export const getCategoryTrees = () => {
    return request.get('/category/listTree')
}

/**
 * 获取文章的分类
 * @param {number} articleId
 */
export const getArticleCategories = (articleId) => {
    return request.get(`/category/${articleId}/categories`)
}

/**
 * 设置文章分类
 * @param {number} articleId
 * @param {Array<number>} categoryIds
 */
export const setArticleCategories = (articleId, categoryIds) => {
    return request.post(`/category/${articleId}/categories`, { categoryIds })
}


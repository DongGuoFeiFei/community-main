// api/collectApi.js
import request from '@/utils/request.js';


// 获取用户收藏夹列表
export const fetchFolders = () => {
    return request.get('/favorite/folders')
}

// 创建新收藏夹
export const createFolder = (data) => {
    return request.post('/favorite/folders', data)
}

// 收藏文章
export const collectArticle = (data) => {
    return request.post('/favorite', data)
}

// 取消收藏
export const cancelCollect = (articleId) => {
    return request.delete(`/favorite/${articleId}`)
}

// 更新收藏夹名称
export const updateFolder = (folderId, data) => {
    return request.put(`/favorite/folders/${folderId}`, data)
}

// 删除收藏夹
export const deleteFolder = (folderId) => {
    return request.delete(`/favorite/folders/${folderId}`)
}

// 获取收藏的文章列表
export const fetchFavoriteArticles = (params) => {
    return request.get('/favorite/articles', {params})
}

// 移动收藏文章到其他收藏夹
export const moveFavorite = (articleId, folderId, activeFolderId) => {
    return request.put(`/favorite/move`, {articleId, folderId, activeFolderId})
}
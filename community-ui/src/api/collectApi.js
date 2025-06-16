// api/collectApi
import request from "@/utils/request.js";

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
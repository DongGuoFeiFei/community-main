import request from '@/utils/request'

// 获取文章列表
export const getArticleList = (params) => {
    return request({
        url: '/admin/article/list',
        method: 'get',
        params
    })
}

// 获取文章详情
export const getArticleDetail = (id) => {
    return request({
        url: `/admin/article/detail/${id}`,
        method: 'get'
    })
}

// 创建文章
export const createArticle = (data) => {
    return request({
        url: '/admin/article/create',
        method: 'post',
        data
    })
}

// 更新文章
export const updateArticle = (data) => {
    return request({
        url: '/admin/article/update',
        method: 'post',
        data
    })
}

// 删除文章
export const deleteArticle = (id) => {
    return request({
        url: `/admin/article/delete/${id}`,
        method: 'post'
    })
}

// 修改文章状态
export const changeArticleStatus = (data) => {
    return request({
        url: '/admin/article/changeStatus',
        method: 'post',
        data
    })
}
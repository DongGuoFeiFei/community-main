import request from '@/utils/request'

// 获取标签列表
export const getTags = () => {
    return request({
        url: '/admin/article/tag/list',
        method: 'get'
    })
}

// 创建标签
export const createTag = (data) => {
    return request({
        url: '/admin/article/tag/create',
        method: 'post',
        data
    })
}

// 更新标签
export const updateTag = (data) => {
    return request({
        url: '/admin/article/tag/update',
        method: 'post',
        data
    })
}

// 删除标签
export const deleteTag = (id) => {
    return request({
        url: `/admin/article/tag/delete/${id}`,
        method: 'post'
    })
}
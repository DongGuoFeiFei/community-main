import request from '@/utils/request'

// 获取分类列表
export const getCategories = () => {
    return request({
        url: '/admin/article/category/list',
        method: 'get'
    })
}

// 创建分类
export const createCategory = (data) => {
    return request({
        url: '/admin/article/category/create',
        method: 'post',
        data
    })
}

// 更新分类
export const updateCategory = (data) => {
    return request({
        url: '/admin/article/category/update',
        method: 'post',
        data
    })
}

// 删除分类
export const deleteCategory = (id) => {
    return request({
        url: `/admin/article/category/delete/${id}`,
        method: 'post'
    })
}
import request from '@/utils/request'

export const getAllTags = () => {
    return request.get(`/tag/getAllTags`)
}

export const getPopularTags = () => {
    return request.get(`/tag/getPopularTags`)
}
export const createTag = (data) => {
    return request.post(`/tag/createTag`, data)
}
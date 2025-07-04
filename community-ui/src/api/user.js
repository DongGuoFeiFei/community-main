import request from '@/utils/request'

export const getUserInfo = (articleId) => {
    return request.get(`/user/${articleId}/info`)
}

export const followUser = (userId) => {
    return request.post(`/user/${userId}/follow`)
}

export const unfollowUser = (userId) => {
    return request.delete(`/user/${userId}/follow`)
}
export const getUserStats = (userId) => {
    return request.get(`/user/${userId}/follow`)
}
import request from '@/utils/request'

export const getUserProfile = (userId) => {
    return request.get(`/user/profile/${userId}`)
}

export const getFollowingList = (userId) => {
    return request.get(`/follow/${userId}/following`)
}

export const getFollowerList = (userId) => {
    return request.get(`/follow/${userId}/followers`)
}

export const getUserPosts = (userId) => {
    return request.get(`/posts/${userId}/posts`)
}

export const getUserFavorites = (userId) => {
    return request.get(`/posts/${userId}/favorites`)
}

export const followUser = (userId) => {
    return request.post(`/user/follow/${userId}`)
}
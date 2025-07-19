// api/follow.js

import request from "@/utils/request.js";

export const addFollowAuthor = (id) => {
    return request.post(`/follow/addFollowAuthor/${id}`)
}

export const delFollowAuthor = (id) => {
    return request.delete(`/follow/delFollowAuthor/${id}`)
}

export const isFollowingAuthor = (id) => {
    return request.get(`/follow/isFollowing/${id}`)
}

// 获取粉丝列表
export const getFollowingList = (userId) => {
    return request.get(`/follow/${userId}/following`)
}

// 获取关注列表
export const getFollowerList = (userId) => {
    return request.get(`/follow/${userId}/followers`)
}

// 移除粉丝
export const removeFan = (fanId) => {
    return request.delete(`/follow/fan/${fanId}`)
}

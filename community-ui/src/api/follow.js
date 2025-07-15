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

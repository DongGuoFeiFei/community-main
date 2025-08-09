import request from '@/utils/request'


export const updateUserProfile = (formData) => {
    return request.post('/user/updateUserProfile', formData).then((res) => {
        if (res.code === 200) {
            console.log(res)
            return true
        } else {
            throw new Error(res.message)
        }
    })
}

export const updateUserCoverId = (data) => {
    return request.post("/user/updateUserCoverId", data).then(res => {
        if (res.code === 200) {
            return true
        } else {
            throw new Error(res.message)
        }
    })
}

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
    return request.get(`/user/${userId}/stats`)
}

// api/sessionStores.js
import request from '../utils/request';

/**
 * 用户登录接口
 * @param {Object} data 登录所需的参数
 * @returns {Promise} 登录请求的 Promise
 */
export const login = (data) => {
    return request.post('/auth/login', data).then(res => {
        if (res.code === 200) {
            localStorage.setItem('token', res.data.token)
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};

export const fetchPosts = (params) => {
    return request.get('/posts', {params}).then(res => {
        if (res.code === 200) {
            return {
                rows: res.rows.map(item => ({
                    id: item.articleId,
                    title: item.title,
                    author: item.nickname,
                    summary: item.content,
                    date: item.createdAt,
                    cover: item.imageUrl
                })),
                total: res.total
            }
        } else {
            throw new Error(res.msg)
        }
    })
}

export const fetchPostDetail = (id) => {
    return request.get(`/posts/${id}`).then(res => {
        if (res.code === 200) {
            // 直接返回 res.data 而不是 res
            return res  // 兼容两种返回格式
        } else {
            throw new Error(res.msg || '获取详情失败')
        }
    })
}

export const uploadFile = (data) => {
    return request({
        url: '/files/upload',
        method: 'post',
        data,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};

export const delFileById = (data) => {
    return request.delete("/files/delFileById", data).then(res => {
        if (res.code === 200) {
            console.log(res)
            return true;
        } else {
            throw new Error(res.message)
        }
    })
}

export const addArticle = (data) => {
    return request.post("/posts/addArticle", data).then(res => {
        if (res.code === 200) {
            console.log(res)
            return true
        } else {
            throw new Error(res.message)
        }
    })
}


// api/sessionStores.js
export const fetchCommentsByPostId = async (postId) => {
    try {
        const res = await request.get(`/comments/getCommentsById`, {params: {postId}})
        if (res.code === 200) {
            return res
        } else {
            throw new Error(res.message || '获取评论失败')
        }
    } catch (error) {
        throw error
    }
}

export const submitCommentToPost = async (postId, {content, parentId = null}) => {
    try {
        const res = await request.post('/comments/addComment', {
            articleId: postId,
            content,
            parentId
        })
        if (res.code === 200) {
            return res
        } else {
            throw new Error(res.message || '评论失败')
        }
    } catch (error) {
        throw error
    }
}




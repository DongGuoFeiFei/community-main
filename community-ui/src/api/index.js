// api/index.js
import request from '../utils/request';



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

export const uploadFile = (formData) => {
    return request({
        url: '/files/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }).then(res => {
        if (res.code === 200) {
            return res; // 返回上传结果数据
        } else {
            throw new Error(res.message || '文件上传失败');
        }
    });
};

export const delFileById = (data) => {
    console.log(data)
    return request.post(`/files/delFileById/${data}`).then(res => {
        if (res.code === 200) {
            return true;
        } else {
            throw new Error(res.message)
        }
    })
}

export const addArticle = (data) => {
    return request.post("/posts/addArticle", data).then(res => {
        if (res.code === 200) {
            return true
        } else {
            throw new Error(res.message)
        }
    })
}


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

export const submitCommentToPost = async (postId, {content, parentId, firstId}) => {
    try {
        const res = await request.post('/comments/addComment', {
            articleId: postId,
            content,
            parentId,
            firstId
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

// 获取我的文章列表
export const getMyArticles = (params) => {
    return request.get('/posts/getArticleList', {params}).then(res => {
        if (res.code === 200) {
            // 直接返回 res.data 而不是 res
            return res  // 兼容两种返回格式
        } else {
            throw new Error(res.msg || '获取我的文章列表')
        }
    })
}

// 删除文章
export const deleteArticle = (id) => {
    return request.delete(`/posts/del/${id}`).then(res => {
        if (res.code === 200) {
            // 直接返回 res.data 而不是 res
            return res  // 兼容两种返回格式
        } else {
            throw new Error(res.msg || '删除文章失败')
        }
    })
}

// 获取文章详情
export const getArticleById = (id) => {
    return request.get(`/posts/getArticleDtl/${id}`).then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};

// 更新文章
export const updateArticle = (id, data) => {
    return request.put(`/posts/updateArticleDtl/${id}`, data).then(res => {
        if (res.code === 200) {
            return res;
        } else {
            throw new Error(res.msg);
        }
    });
};

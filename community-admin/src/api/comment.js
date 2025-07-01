import request from '../utils/request';

// 获取评论列表
export const getCommentList = (params) => {
    return request.get('/admin/comments', {params});
};

// 删除评论
export const deleteComment = (id) => {
    return request.delete(`/admin/comments/${id}`);
};

// 批量删除评论
export const batchDeleteComments = (ids) => {
    return request.post('/admin/comments/batch-delete', {ids});
};

// 审核评论
export const approveComment = (id) => {
    return request.put(`/admin/comments/${id}/approve`);
};

// 回复评论
export const replyComment = (id, content) => {
    return request.post(`/admin/comments/${id}/reply`, {content});
};


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
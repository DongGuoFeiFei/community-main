import request from '../utils/request';

// 获取评论列表
export const getCommentList = (params) => {
    return request.get('/comments/admin', {params});
};

// 删除评论
export const deleteComment = (id) => {
    return request.delete(`/comments/${id}`);
};

// 批量删除评论
export const batchDeleteComments = (ids) => {
    return request.post('/comments/batch-delete/admin', {ids});
};

// 审核评论
export const approveComment = (id) => {
    return request.put(`/comments/${id}/approve/admin`);
};

// 回复评论
export const replyComment = (id, content) => {
    return request.post(`/comments/${id}/reply/admin`, {content});
};


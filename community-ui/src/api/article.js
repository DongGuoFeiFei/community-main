import request from '@/utils/request';

// 获取相关文章
export const getRelatedPosts = (articleId) => {
    return request.get(`/article/${articleId}/related`);
};

// 获取热门标签
export const getHotTags = () => {
    return request.get('/tags/hot');
};
export const sharePost = () => {
    return request.get('/share/sharePost');
};

// 获取我的文章列表
export const getMyArticles = (params) => {
    return request.get('/posts/getArticleList', {params}).then(res => {
        if (res.code === 200) {
            return res  // 兼容两种返回格式
        } else {
            throw new Error(res.msg || '获取我的文章列表')
        }
    })
}

/**
 * 更新文章评论区状态
 * @param {number} articleId 文章ID
 * @param {number} status 状态：0-关闭，1-开启
 * @returns {Promise}
 */
export const updateArticleCommentStatus = (articleId, status) => {
    return request.put(`/posts/${articleId}/comment-status`, {status: status});
};

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

/**
 * 获取文章标签
 * @param {number} postId 文章ID
 * @returns {Promise}
 */
export const getPostTags = (postId) => {
    return request.get(`/posts/${postId}/tags`)
}
export const getHotPosts = () => {
    return request.get(`/posts/hotPosts`)
}


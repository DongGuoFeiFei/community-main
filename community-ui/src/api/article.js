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


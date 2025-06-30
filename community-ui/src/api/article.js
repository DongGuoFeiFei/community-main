import request from '@/utils/request';

// 获取相关文章
export const getRelatedPosts = (articleId) => {
    return request.get(`/article/${articleId}/related`);
};

// 获取热门标签
export const getHotTags = () => {
    return request.get('/tags/hot');
};
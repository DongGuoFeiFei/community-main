import request from '@/utils/request';

// 获取话题列表
export const getTopics = (params) => {
    return request.get('/admin/topics', { params });
};

// 新增话题
export const addTopic = (data) => {
    return request.post('/admin/topics', data);
};

// 更新话题
export const updateTopic = (id, data) => {
    return request.put(`/admin/topics/${id}`, data);
};

// 删除话题
export const deleteTopic = (id) => {
    return request.delete(`/admin/topics/${id}`);
};

// 批量删除话题
export const batchDeleteTopics = (ids) => {
    return request.delete('/admin/topics/batch', { data: { ids } });
};
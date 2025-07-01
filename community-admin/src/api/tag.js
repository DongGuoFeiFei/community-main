import request from '../utils/request';

export const getTagList = (params) => {
    return request.get('/admin/tags', { params });
};

export const createTag = (data) => {
    return request.post('/admin/tags', data);
};

export const updateTag = (id, data) => {
    return request.put(`/admin/tags/${id}`, data);
};

export const deleteTag = (id) => {
    return request.delete(`/admin/tags/${id}`);
};

export const batchDeleteTags = (ids) => {
    return request.delete('/admin/tags/batch', { data: { ids } });
};
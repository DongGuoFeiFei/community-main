import request from '../utils/request';

export const getActivityList = (params) => {
    return request.get('/activity/list', { params });
};

export const getActivityDetail = (id) => {
    return request.get(`/activity/detail/${id}`);
};

export const createActivity = (data) => {
    return request.post('/activity/create', data);
};

export const updateActivity = (data) => {
    return request.put('/activity/update', data);
};

export const deleteActivity = (id) => {
    return request.delete(`/activity/delete/${id}`);
};

export const changeActivityStatus = (id, status) => {
    return request.put('/activity/status', { id, status });
};


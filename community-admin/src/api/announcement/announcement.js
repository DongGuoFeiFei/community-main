import request from '@/utils/request';

/**
 * 获取公告列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getAnnouncementList = (params) => {
    return request.get('/system/announcement/list', { params });
};

/**
 * 获取公告详情
 * @param {Number} id 公告ID
 * @returns {Promise}
 */
export const getAnnouncementDetail = (id) => {
    return request.get(`/system/announcement/${id}`);
};

/**
 * 新增公告
 * @param {Object} data 公告数据
 * @returns {Promise}
 */
export const addAnnouncement = (data) => {
    return request.post('/system/announcement', data);
};

/**
 * 更新公告
 * @param {Object} data 公告数据
 * @returns {Promise}
 */
export const updateAnnouncement = (data) => {
    return request.put('/system/announcement', data);
};

/**
 * 删除公告
 * @param {Number} id 公告ID
 * @returns {Promise}
 */
export const deleteAnnouncement = (id) => {
    return request.delete(`/system/announcement/${id}`);
};
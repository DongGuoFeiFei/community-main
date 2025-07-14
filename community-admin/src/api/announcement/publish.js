import request from '@/utils/request';

/**
 * 发布公告
 * @param {Number} id 公告ID
 * @returns {Promise}
 */
export const publishAnnouncement = (id) => {
    return request.post(`/system/announcement/publish/${id}`);
};

/**
 * 下线公告
 * @param {Number} id 公告ID
 * @returns {Promise}
 */
export const offlineAnnouncement = (id) => {
    return request.post(`/system/announcement/offline/${id}`);
};
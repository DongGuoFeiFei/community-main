// /api/system/announcement.js
import request from '@/utils/request';

/**
 * 获取当前有效的公告列表
 * @returns {Promise<Array>} 公告列表
 */
export const getActiveAnnouncements = () => {
    return request.get('/system/announcement/active').then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};

/**
 * 获取最新的一条有效公告
 * @returns {Promise<Object|null>} 最新公告或null
 */
export const getLatestAnnouncement = () => {
    return request.get('/system/announcement/latest').then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};
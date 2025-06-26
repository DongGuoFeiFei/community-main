import request from '../utils/request';

/**
 * 获取通知列表
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @param {string} params.type - 通知类型(可选)
 * @returns {Promise} 通知列表
 */
export const getNotifications = (params) => {
    return request.get('/notifications', { params });
};

/**
 * 标记通知为已读
 * @param {number|Array} ids - 通知ID或ID数组
 * @returns {Promise} 操作结果
 */
export const markAsRead = (ids) => {
    return request.put('/notifications/read', { ids: Array.isArray(ids) ? ids : [ids] });
};

/**
 * 删除通知
 * @param {number|Array} ids - 通知ID或ID数组
 * @returns {Promise} 操作结果
 */
export const deleteNotifications = (ids) => {
    return request.delete('/notifications', { data: { ids: Array.isArray(ids) ? ids : [ids] } });
};

/**
 * 获取未读通知数量
 * @returns {Promise} 未读数量
 */
export const getUnreadCount = () => {
    return request.get('/notifications/unread-count');
};
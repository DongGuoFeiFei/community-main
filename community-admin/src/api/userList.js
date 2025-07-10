// /api/user/index.js
import request from '@/utils/request';

/**
 * 获取用户列表
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export const getUsers = (params) => {
    return request.get('/admin/users', {params});
};

/**
 * 获取用户详情
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export const getUserDetail = (userId) => {
    return request.get(`/admin/users/${userId}`);
};

/**
 * 更新用户信息
 * @param {number} userId - 用户ID
 * @param {Object} data - 更新数据
 * @returns {Promise}
 */
export const updateUser = (userId, data) => {
    return request.put(`/admin/users/${userId}`, data);
};

/**
 * 删除用户
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export const deleteUser = (userId) => {
    return request.delete(`/admin/users/${userId}`);
};

/**
 * 批量删除用户
 * @param {Array} userIds - 用户ID数组
 * @returns {Promise}
 */
export const batchDeleteUsers = (userIds) => {
    return request.post('/admin/users/batch-delete', {userIds});
};

export const activeChange = (data) => {
    return request.post('/admin/users/active-change', {data});
};
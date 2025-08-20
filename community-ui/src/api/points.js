import request from '@/utils/request';

/**
 * 获取用户积分账户信息
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export const getPointsAccount = (userId) => {
  return request.get(`/points/account/${userId}`);
};

/**
 * 更新用户积分账户
 * @param {number} userId - 用户ID
 * @param {Object} data - 更新数据
 * @returns {Promise}
 */
export const updatePointsAccount = (userId, data) => {
  return request.put(`/points/account/${userId}`, data);
};

/**
 * 获取用户积分交易记录
 * @param {number} userId - 用户ID
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export const getPointsTransactions = (userId, params = {}) => {
  return request.get(`/points/transactions/${userId}`, {params});
};

/**
 * 添加积分交易记录
 * @param {Object} data - 交易数据
 * @returns {Promise}
 */
export const addPointsTransaction = (data) => {
  return request.post('/points/transactions', data);
};


/**
 * 获取积分等级列表
 * @returns {Promise}
 */
export const getPointsLevels = () => {
  return request.get('/points/levels');
};

/**
 * 获取用户当前等级信息
 * @param {number} levelId - 等级ID
 * @returns {Promise}
 */
export const getUserLevelInfo = (levelId) => {
  return request.get(`/points/levels/${levelId}`);
};

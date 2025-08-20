import request from '@/utils/request';

/**
 * 获取用户积分账户列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getPointsAccounts = (params) => {
  return request.get('/points/accounts', { params });
};

/**
 * 获取单个用户积分账户详情
 * @param {number} accountId 账户ID
 * @returns {Promise}
 */
export const getPointsAccountDetail = (accountId) => {
  return request.get(`/points/accounts/${accountId}`);
};

/**
 * 调整用户积分
 * @param {number} accountId 账户ID
 * @param {Object} data 调整数据
 * @returns {Promise}
 */
export const adjustPoints = (accountId, data) => {
  return request.post(`/points/accounts/${accountId}/adjust`, data);
};

/**
 * 冻结/解冻积分
 * @param {number} accountId 账户ID
 * @param {Object} data 操作数据
 * @returns {Promise}
 */
export const freezePoints = (accountId, data) => {
  return request.post(`/points/accounts/${accountId}/freeze`, data);
};

/**
 * 获取积分记录列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getPointsLogs = (params) => {
  return request.get('/points/logs', { params });
};

/**
 * 撤销积分记录
 * @param {number} logId 记录ID
 * @returns {Promise}
 */
export const revokePointsLog = (logId) => {
  return request.post(`/points/logs/${logId}/revoke`);
};

/**
 * 获取积分消费记录
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getPointsConsumptions = (params) => {
  return request.get('/points/consumptions', { params });
};

/**
 * 取消积分消费
 * @param {number} consumptionId 消费记录ID
 * @returns {Promise}
 */
export const cancelPointsConsumption = (consumptionId) => {
  return request.post(`/points/consumptions/${consumptionId}/cancel`);
};

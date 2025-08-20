import request from '@/utils/request'

/**
 * 获取举报列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getReportList = (params) => {
  return request.get('/report', { params })
}

/**
 * 获取举报详情
 * @param {number} reportId 举报ID
 * @returns {Promise}
 */
export const getReportDetail = (reportId) => {
  return request.get(`/report/${reportId}`)
}

/**
 * 处理举报
 * @param {Object} data 处理数据
 * @returns {Promise}
 */
export const processReport = (data) => {
  return request.post('/report/process', data)
}

/**
 * 忽略举报
 * @param {number} reportId 举报ID
 * @returns {Promise}
 */
export const ignoreReport = (reportId) => {
  return request.post(`/report/${reportId}/ignore`)
}

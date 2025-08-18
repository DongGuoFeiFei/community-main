import request from '@/utils/request'

/**
 * 获取用户列表（带分页和搜索）
 * @param {Object} params - 查询参数
 * @param {string} [params.username] - 用户名
 * @param {string} [params.email] - 邮箱
 * @param {string} [params.status] - 状态: '0'-禁用, '1'-启用
 * @param {number} [params.roleId] - 角色ID
 * @param {number} [params.page] - 页码
 * @param {number} [params.size] - 每页数量
 * @returns {Promise}
 */
export const getUserList = (params = {}) => {
  return request.get('admin/users/list', { params })
}

/**
 * 获取角色列表
 * @returns {Promise}
 */
export const getRoleList = () => {
  return request.get('/role/getRoleList')
}

/**
 * 获取用户角色
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export const getUserRoles = (userId) => {
  return request.get(`/user/roles/${userId}`)
}

/**
 * 更新用户角色
 * @param {Object} data - 更新数据
 * @param {number} data.userId - 用户ID
 * @param {Array} data.roleIds - 角色ID数组
 * @returns {Promise}
 */
export const updateUserRoles = (data) => {
  return request.put('/user/roles', data)
}

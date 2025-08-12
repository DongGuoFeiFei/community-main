import request from '@/utils/request';

/**
 * 获取角色列表
 */
export const getRoleList = (params) => {
  return request.get('/role/list', { params });
};

/**
 * 添加角色
 */
export const addRole = (data) => {
  return request.post('/role/add', data);
};

/**
 * 更新角色
 */
export const updateRole = (data) => {
  return request.put('/role/update', data);
};

/**
 * 删除角色
 */
export const deleteRole = (roleId) => {
  return request.delete(`/role/delete/${roleId}`);
};

/**
 * 获取角色详情
 */
export const getRoleDetail = (roleId) => {
  return request.get(`/role/detail/${roleId}`);
};

/**
 * 修改角色状态
 */
export const changeRoleStatus = (roleId, status) => {
  return request.put(`/role/status/${roleId}/${status}`);
};

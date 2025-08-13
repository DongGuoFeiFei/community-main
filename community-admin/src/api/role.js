import request from '@/utils/request';

/**
 * 获取角色列表
 */
export const getRoleList = (params) => {
  return request.get('/role/list', {params});
};

/**
 * 获取角色详情
 */
export const getRoleDetail = (roleId) => {
  return request.get(`/role/${roleId}`);
};

/**
 * 创建角色
 */
export const createRole = (data) => {
  return request.post('/role', data);
};

/**
 * 更新角色
 */
export const updateRole = (data) => {
  return request.put('/role', data);
};

/**
 * 删除角色
 */
export const deleteRole = (roleIds) => {
  return request.delete(
    '/role',
    {
      data: {ids: roleIds}
    });
};

/**
 * 修改角色状态
 */
export const changeRoleStatus = (roleId, status) => {
  return request.put('/role/status', {id: roleId, status});
};

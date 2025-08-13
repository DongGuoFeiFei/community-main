import request from '@/utils/request';

/**
 * 获取角色数据权限
 */
export const getRoleDataScope = (roleId) => {
  return request.get(`/role/data-scope/${roleId}`);
};

/**
 * 更新角色数据权限
 */
export const updateRoleDataScope = (data) => {
  return request.put('/role/data-scope', data);
};

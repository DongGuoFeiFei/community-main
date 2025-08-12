import request from '@/utils/request';

/**
 * 获取角色数据权限
 */
export const getRoleDataScope = (roleId, resourceType) => {
  return request.get(`/role/dataScope/${roleId}/${resourceType}`);
};

/**
 * 更新角色数据权限
 */
export const updateRoleDataScope = (data) => {
  return request.put('/role/dataScope/update', data);
};

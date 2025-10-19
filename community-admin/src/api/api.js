// /api/role/api.js
import request from '@/utils/request';

// 获取角色API权限
export const getRoleApis = (roleId) => {
  return request.get(`/apiPermission/${roleId}`);
};

// 更新角色API权限
export const updateRoleApis = (roleId, apiIds) => {
  return request.put(`/role/apiPermission`, {id: roleId, ids: apiIds});
};

// 获取API树
export const getApiTree = () => {
  return request.get('/apiPermission/tree');
};

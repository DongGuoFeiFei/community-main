// /api/role/api.js
import request from '@/utils/request';

// 获取角色API权限
export const getRoleApis = (roleId) => {
  return request.get(`/role/api/${roleId}`);
};

// 更新角色API权限
export const updateRoleApis = (roleId, apiIds) => {
  return request.put(`/role/api/${roleId}`, {apiIds});
};

// 获取API树
export const getApiTree = () => {
  return request.get('/api/tree');
};

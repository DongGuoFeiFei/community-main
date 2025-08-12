import request from '@/utils/request';

/**
 * 获取角色关联用户
 */
export const getRoleUsers = (roleId) => {
  return request.get(`/role/users/${roleId}`);
};

/**
 * 分配用户给角色
 */
export const assignUsersToRole = (roleId, userIds) => {
  return request.post(`/role/users/assign/${roleId}`, { userIds });
};

/**
 * 从角色移除用户
 */
export const removeUsersFromRole = (roleId, userIds) => {
  return request.post(`/role/users/remove/${roleId}`, { userIds });
};

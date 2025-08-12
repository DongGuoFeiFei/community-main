import request from '@/utils/request';

/**
 * 获取菜单树
 */
export const getMenuTree = () => {
  return request.get('/menu/tree');
};

/**
 * 获取用户菜单树
 */
export const getUserMenuTree = () => {
  return request.get('/menu/getUserMenuTree')
};

/**
 * 获取角色菜单权限
 */
export const getRoleMenus = (roleId) => {
  return request.get(`/role/menus/${roleId}`);
};

/**
 * 更新角色菜单权限
 */
export const updateRoleMenus = (roleId, menuIds) => {
  return request.put(`/role/menus/${roleId}`, {menuIds});
};

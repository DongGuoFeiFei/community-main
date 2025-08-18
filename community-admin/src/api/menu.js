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
  return request.get(`/menu/${roleId}`);
};

/**
 * 更新角色菜单权限
 */
export const updateRoleMenus = (roleId, menuIds) => {
  return request.put('/role/menu', {id: roleId, ids: menuIds});
};

// 获取菜单列表
export const getMenuList = (params) => {
  return request.get('/menu/list', {params})
}

// 添加菜单
export const addMenu = (data) => {
  return request.post('/menu', data)
}

// 更新菜单
export const updateMenu = (data) => {
  return request.put('/menu', data)
}

// 删除菜单
export const deleteMenu = (menuId) => {
  return request.delete(`/menu/${menuId}`)
}

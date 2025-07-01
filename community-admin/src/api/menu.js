import request from '@/utils/request';

// 获取后台菜单配置
export const getAdminMenu = () => {
    return request.get('/admin/menu');
};

// 更新菜单配置
export const updateMenuConfig = (data) => {
    return request.put('/admin/menu', data);
};

// 获取菜单权限配置
export const getMenuPermissions = () => {
    return request.get('/admin/menu/permissions');
};
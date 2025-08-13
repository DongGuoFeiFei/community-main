// /api/dept.js
import request from '@/utils/request';

// 获取部门树
export const getDeptTree = () => {
  return request.get('/dept/tree');
};

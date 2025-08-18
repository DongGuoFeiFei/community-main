// api/category.js
import request from '../utils/request';

/**
 * 获取分类列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export const getCategories = (params) => {
  return request.get('/category', { params });
};

/**
 * 获取分类树形结构
 * @returns {Promise}
 */
export const getCategoryTree = () => {
  return request.get('/category/tree');
};

/**
 * 添加分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export const addCategory = (data) => {
  return request.post('/category', data);
};

/**
 * 更新分类
 * @param {Object} data 分类数据
 * @returns {Promise}
 */
export const updateCategory = (data) => {
  return request.put(`/category/${data.category_id}`, data);
};

/**
 * 删除分类
 * @param {number} categoryId 分类ID
 * @returns {Promise}
 */
export const deleteCategory = (categoryId) => {
  return request.delete(`/category/${categoryId}`);
};

/**
 * 批量更新分类排序
 * @param {Array} sortData 排序数据
 * @returns {Promise}
 */
export const batchUpdateSort = (sortData) => {
  return request.put('/category/sort', sortData);
};


import request from '@/utils/request'

// 获取等级列表
export const getPointsLevels = (params) => {
  return request.get('/points/levels', { params })
}

// 新增等级
export const addPointsLevel = (data) => {
  return request.post('/points/levels', data)
}

// 更新等级
export const updatePointsLevel = (data) => {
  return request.put('/points/levels', data)
}

// 删除等级
export const deletePointsLevel = (id) => {
  return request.delete(`/points/levels/${id}`)
}

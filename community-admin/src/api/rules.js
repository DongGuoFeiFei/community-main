import request from '@/utils/request'

// 获取积分规则列表
export const getPointsRules = (params) => {
  return request.get('/points/rules', { params })
}

// 新增积分规则
export const addPointsRule = (data) => {
  return request.post('/points/rules', data)
}

// 更新积分规则
export const updatePointsRule = (data) => {
  return request.put('/points/rules', data)
}

// 删除积分规则
export const deletePointsRule = (id) => {
  return request.delete(`/points/rules/${id}`)
}

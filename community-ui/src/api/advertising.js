import request from '@/utils/request'

/**
 * 获取广告列表
 * @param {Object} params
 * @returns {Promise}
 */
export const getAdvertising = (params) => {
    return request.get('/advertising/list', { params })
}

/**
 * 记录广告点击
 * @param {Number} adId
 * @returns {Promise}
 */
export const recordAdClick = (adId) => {
    return request.post('/advertising/click', { adId })
}
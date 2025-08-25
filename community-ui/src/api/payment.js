import request from '@/utils/request'

/**
 * 创建支付宝支付
 * @param {Object} paymentData 支付数据
 * @returns {Promise<string>} 支付表单HTML
 */
export const createAlipayPayment = (paymentData) => {
    return request.post('/api/payment/alipay/create', paymentData)
        .then(res => {
            if (res.code === 200) {
                return res.data
            } else {
                throw new Error(res.msg || '支付创建失败')
            }
        })
        .catch(error => {
            console.error('支付宝支付API错误:', error)
            throw new Error(error.message || '网络错误，请稍后重试')
        })
}

/**
 * 查询支付状态
 * @param {string} outTradeNo 商户订单号
 * @returns {Promise<Object>} 支付状态信息
 */
export const queryPaymentStatus = (outTradeNo) => {
    return request.get(`/api/payment/status/${outTradeNo}`)
        .then(res => {
            if (res.code === 200) {
                return res.data
            } else {
                throw new Error(res.msg || '查询支付状态失败')
            }
        })
}

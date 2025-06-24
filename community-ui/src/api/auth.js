// api/auth.js
import request from '../utils/request';

export const getEmailCode = (data) => {
    return request.post('/auth/registerCode', {
        ...data,
        type: 'register'
    });
};

export const register = (data) => {
    return request.post('/auth/register', data);
};

// 找回密码
export const forgotPassword = (data) => {
    return request.post('/auth/forgot-password', data).then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};

export const resetPassword = (data) => {
    return request.post('/auth/reset-password', data).then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};
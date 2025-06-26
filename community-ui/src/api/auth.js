// api/auth.js
import request from '../utils/request';


export const login = (data) => {
    return request.post('/auth/login', data).then(res => {
        if (res.code === 200) {
            return res.data;
        } else {
            throw new Error(res.msg);
        }
    });
};


// 刷新token
export const refreshToken = () => {
    return request.post("/auth/refreshToken").then(res => {
        if (res.code === 200) {
            return res;
        } else {
            throw new Error(res.msg);
        }
    })
}
export const getCaptcha = () => {
    return request.get('/auth/captcha');
};

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
// utils/request.js
import axios from 'axios';


const request = axios.create({
    baseURL: 'http://127.0.0.1:8080', // 后端接口的基础URL
    timeout: 5000, // 请求超时
    headers: {
        'Content-Type': 'application/json',
    },
});

// 请求拦截器：如果有 token 就添加到请求头
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token'); // 从本地存储获取 token
        if (token) {
            // config.headers['Authorization'] = `Bearer ${token}`;
            // 后台接收token
            config.headers['token'] = `${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器：统一处理响应结果
request.interceptors.response.use(
    response => {
        return response.data; // 直接返回 data
    },
    error => {
        return Promise.reject(error);
    }
);

export default request;

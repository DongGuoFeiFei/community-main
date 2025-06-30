// utils/request.js
import axios from 'axios';
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import config from "@/config.js";


const request = axios.create({
    baseURL: config.apiBaseUrl, // 后端接口的基础URL
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
        if (!error.response) {
            // 网络错误（如请求未到达服务器）
            ElMessage.error('网络异常，请检查网络连接');
            return Promise.reject(error);
        }

        const {data} = error.response;
        switch (data.code) {
            case 401:
                // 场景：Token 过期/未登录
                ElMessage.warning('登录已过期，请重新登录');
                localStorage.removeItem('token'); // 清除旧 Token
                router.push('/login'); // 跳转到登录页
                break;
            case 403:
                // 场景：无权限访问
                ElMessage.error('无权访问此资源');
                break;
            case 404:
                // 场景：接口不存在
                ElMessage.error('请求的资源不存在');
                break;
            case 500:
                // 场景：服务器内部错误
                ElMessage.error(`服务器错误: ${data.message || '未知错误'}`);
                break;
            default:
                ElMessage.error(`请求失败: ${status}`);
        }
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

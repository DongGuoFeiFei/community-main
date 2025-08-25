import axios from 'axios';
import {ElMessage, ElMessageBox} from "element-plus";
import config from "@/utils/env.js";
import router from '@/router';

const request = axios.create({
    baseURL: config.apiBaseUrl,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['token'] = `${token}`;
        }
        return config;
    },
    error => {
        // 请求未发出时的错误（如网络问题）
        ElMessage.error('网络异常，请检查网络连接');
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        // 处理成功的响应
        const {data} = response;

        // 如果后端返回了自定义状态码，且不是200，则视为错误
        if (data && data.code !== undefined && data.code !== 200) {
            handleErrorResponse(data);
            return Promise.reject(data);
        }

        return data;
    },
    error => {
        // 处理HTTP错误状态码
        if (error.response) {
            // 服务器返回了响应，但状态码不在2xx范围内
            const {data} = error.response;

            if (data && data.code !== undefined) {
                // 优先使用后端返回的自定义错误码
                handleErrorResponse(data);
            } else {
                // 没有自定义错误码，使用HTTP状态码
                handleHttpError(error.response.status, error.response.data?.message);
            }
        } else if (error.request) {
            // 请求已发出但没有收到响应
            ElMessage.error('服务器未响应，请稍后重试');
        } else {
            // 请求配置出错
            ElMessage.error('请求配置错误');
        }

        return Promise.reject(error);
    }
);

// 处理后端返回的自定义错误
function handleErrorResponse(data) {
    const {code, msg} = data;

    switch (code) {
        case 401:
            handleUnauthorizedError(msg);
            break;
        case 403:
            ElMessage.error(msg || '无权访问此资源');
            break;
        case 404:
            ElMessage.error(msg || '请求的资源不存在');
            break;
        case 500:
            // ElMessage.error(msg || '服务器内部错误');
            break;
        default:
            ElMessage.error(msg || `请求失败，错误码: ${code}`);
    }
}

// 处理HTTP错误状态码
function handleHttpError(status, message) {
    switch (status) {
        case 400:
            // ElMessage.error(message || '请求参数错误');
            break;
        case 401:
            handleUnauthorizedError(message);
            break;
        case 403:
            ElMessage.error('无权访问此资源');
            break;
        case 404:
            ElMessage.error('请求的资源不存在');
            break;
        case 500:
            // ElMessage.error(message || '服务器内部错误');
            break;
        case 502:
        case 503:
        case 504:
            ElMessage.error('服务器暂时不可用，请稍后重试');
            break;
        default:
            ElMessage.error(`请求失败，状态码: ${status}`);
    }
}

// 全局变量，用于存储登录弹窗实例
let loginDialog = null

// 设置登录弹窗实例的方法
const setLoginDialog = (dialogInstance) => {
    loginDialog = dialogInstance
}


// 专门处理401未授权错误
async function handleUnauthorizedError(errorMsg) {
    const defaultMessage = errorMsg || '登录状态已过期，请重新登录'

    try {
        await ElMessageBox.confirm(
            defaultMessage,
            '会话过期',
            {
                confirmButtonText: '立即登录',
                cancelButtonText: '稍后再说',
                type: 'warning',
                customClass: 'session-expired-dialog',
                closeOnClickModal: false,
                closeOnPressEscape: false,
                showClose: false,
                distinguishCancelAndClose: true
            }
        )

        // 用户确认登录，显示登录弹窗
        if (loginDialog) {
            loginDialog.openDialog()
        } else {
            // 如果弹窗未初始化，回退到跳转登录页
            localStorage.removeItem('token')
            window.location.reload()
        }

    } catch (action) {
        if (action === 'cancel') {
            // 用户选择"稍后再说"
            ElMessage.info('您选择了稍后登录')
        }
    }
}

export default request;
export { setLoginDialog }

import request from '@/utils/request';

// 获取控制台数据
export const getDashboardData = () => {
    return request.get('/dashboard/data');
};

// 清除缓存
export const clearCache = () => {
    return request.post('/dashboard/clear-cache');
};

// 数据备份
export const backupData = () => {
    return request.post('/dashboard/backup');
};
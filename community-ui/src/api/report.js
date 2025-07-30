// api/report.js
import request from '../utils/request';

export const submitReport = (data) => {
    return request.post('/report/submit', data).then(res => {
        if (res.code !== 200) {
            throw new Error(res.msg || '提交举报失败');
        }
        return res.data;
    });
};
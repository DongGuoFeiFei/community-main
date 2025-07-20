// api/files.js
import request from '../utils/request';

export const uploadFile = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    const res = await request.post('/files/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });

    if (res.code === 200) {
        return res.data.accessUrl;
    }
    throw new Error(res.msg || '文件上传失败');
};
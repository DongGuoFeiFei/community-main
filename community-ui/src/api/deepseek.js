// /api/ai/deepseek.js
import request from '@/utils/request';

export const generateText = (data) => {
    return request.post('/api/deepseek/generate', {
            prompt: data
        },
        {
            timeout: 30000
        })
};


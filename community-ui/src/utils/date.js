// /utils/date.js
import dayjs from 'dayjs';

export const formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
    return dayjs(date).format(format);
};
export const formatTime = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
    return dayjs(date).format(format);
};
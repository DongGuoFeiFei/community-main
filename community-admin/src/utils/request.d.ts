// src/utils/request.d.ts
import type { AxiosRequestConfig, AxiosResponse, AxiosInstance } from 'axios';

declare module '@/utils/request' {
    interface RequestResponse<T = any> {
        code: number;
        msg: string;
        data: T;
    }

    const request: AxiosInstance & {
        get<T = any>(url: string, config?: AxiosRequestConfig): Promise<RequestResponse<T>>;
        post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<RequestResponse<T>>;
        put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<RequestResponse<T>>;
        delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<RequestResponse<T>>;
        patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<RequestResponse<T>>;
    };

    export default request;
}
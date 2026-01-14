import type { RequestConfig } from '@umijs/max';
import { message } from 'antd';
import type { Result } from '@/types/common';

// 请求配置
export const requestConfig: RequestConfig = {
  baseURL: '/api',
  timeout: 10000,

  requestInterceptors: [
    (config: any) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
  ],

  responseInterceptors: [
    (response: any) => {
      const res = response.data as Result<unknown>;
      if (res.code === 200) {
        // UmiJS 需要返回完整 response，但修改 data 为解包后的数据
        response.data = res.data;
        return response;
      }
      message.error(res.msg || '请求失败');
      return Promise.reject(new Error(res.msg));
    },
  ],

  errorConfig: {
    errorHandler: (error: any) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      throw error;
    },
  },
};

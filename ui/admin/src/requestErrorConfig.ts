import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';
import { message, notification } from 'antd';

// 错误处理方案： 错误类型
enum ErrorShowType {
  SILENT = 0,
  WARN_MESSAGE = 1,
  ERROR_MESSAGE = 2,
  NOTIFICATION = 3,
  REDIRECT = 9,
}
// 与后端约定的响应数据格式
interface ResponseStructure {
  code: number;
  msg: string;
  data: any;
}

/**
 * @name 错误处理
 * pro 自带的错误处理， 可以在这里做自己的改动
 * @doc https://umijs.org/docs/max/request#配置
 */
export const errorConfig: RequestConfig = {
  // 错误处理： umi@3 的错误处理方案。
  errorConfig: {
    // 错误抛出
    errorThrower: (res) => {
      const { code, msg, data } =
        res as unknown as ResponseStructure;
      if (code !== 200) {
        const error: any = new Error(msg);
        error.name = 'BizError';
        error.info = { code, msg, data };
        throw error; // 抛出自制的错误
      }
    },
    // 错误接收及处理
    errorHandler: (error: any, opts: any) => {
      if (opts?.skipErrorHandler) throw error;
      // 我们的 errorThrower 抛出的错误。
      if (error.name === 'BizError') {
        const errorInfo: ResponseStructure | undefined = error.info;
        if (errorInfo) {
          const { msg, code } = errorInfo;
          // 401 未授权，跳转登录
          if (code === 401) {
            message.error('登录已过期，请重新登录');
            localStorage.removeItem('token');
            window.location.href = '/user/login';
            return;
          }
          message.error(msg || '请求失败');
        }
      } else if (error.response) {
        // Axios 的错误
        // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
        if (error.response.status === 401) {
          message.error('登录已过期，请重新登录');
          localStorage.removeItem('token');
          window.location.href = '/user/login';
        } else {
          message.error(`请求失败: ${error.response.status}`);
        }
      } else if (error.request) {
        // 请求已经成功发起，但没有收到响应
        message.error('网络异常，请稍后重试');
      } else {
        // 发送请求时出了点问题
        message.error('请求错误，请稍后重试');
      }
    },
  },

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // 从 localStorage 获取 token 并添加到请求头
      const token = localStorage.getItem('token');
      if (token) {
        config.headers = {
          ...config.headers,
          'Authorization': `Bearer ${token}`,
        };
      }
      return config;
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      // 拦截响应数据，进行个性化处理
      const { data } = response as unknown as { data: ResponseStructure };
      
      // 如果后端返回的 code 不是 200，则认为请求失败
      if (data && data.code !== 200) {
        // 这里不做处理，交给 errorThrower 处理
      }
      
      return response;
    },
  ],
};

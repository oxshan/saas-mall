import { request } from '@umijs/max';

/**
 * 用户登录
 */
export async function login(data: API.LoginReq) {
  return request<API.Result<API.LoginRsp>>('/auth/login', {
    method: 'POST',
    data,
  });
}

/**
 * 获取当前用户信息
 */
export async function getCurrentUser(options?: { [key: string]: any }) {
  return request<API.Result<API.CurrentUser>>('/auth/current-user', {
    method: 'GET',
    ...(options || {}),
  });
}

import { request } from '@umijs/max';
import type { LoginParams, LoginResult, UserInfo } from '@/types/user';

// 登录
export async function login(params: LoginParams) {
  return request<LoginResult>('/admin/auth/login', {
    method: 'POST',
    data: params,
  });
}

// 获取当前用户信息
export async function getCurrentUser() {
  return request<UserInfo>('/admin/auth/current-user', {
    method: 'GET',
  });
}

// 退出登录
export async function logout() {
  localStorage.removeItem('token');
  window.location.href = '/login';
}

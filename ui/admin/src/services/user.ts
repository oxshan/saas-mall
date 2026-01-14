import { request } from '@umijs/max';
import type { PageParams, PageResult } from '@/types/common';

export interface UserItem {
  id: number;
  username: string;
  nickname: string;
  email?: string;
  phone?: string;
  status: number;
  roleIds: number[];
  createTime: string;
}

// 获取用户列表
export async function getUserList(params: PageParams & { username?: string }) {
  return request<PageResult<UserItem>>('/admin/user/page', {
    method: 'GET',
    params,
  });
}

// 创建用户
export async function createUser(data: Partial<UserItem> & { password: string }) {
  return request('/admin/user', { method: 'POST', data });
}

// 更新用户
export async function updateUser(id: number, data: Partial<UserItem>) {
  return request(`/admin/user/${id}`, { method: 'PUT', data });
}

// 删除用户
export async function deleteUser(id: number) {
  return request(`/admin/user/${id}`, { method: 'DELETE' });
}

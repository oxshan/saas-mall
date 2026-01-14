import { request } from '@umijs/max';
import type { PageParams, PageResult } from '@/types/common';

export interface RoleItem {
  id: number;
  name: string;
  code: string;
  status: number;
  remark?: string;
  menuIds: number[];
  createTime: string;
}

// 获取角色列表
export async function getRoleList(params: PageParams & { name?: string }) {
  return request<PageResult<RoleItem>>('/admin/role/page', {
    method: 'GET',
    params,
  });
}

// 获取所有角色
export async function getAllRoles() {
  return request<RoleItem[]>('/admin/role/list', { method: 'GET' });
}

// 创建角色
export async function createRole(data: Partial<RoleItem>) {
  return request('/admin/role', { method: 'POST', data });
}

// 更新角色
export async function updateRole(id: number, data: Partial<RoleItem>) {
  return request(`/admin/role/${id}`, { method: 'PUT', data });
}

// 删除角色
export async function deleteRole(id: number) {
  return request(`/admin/role/${id}`, { method: 'DELETE' });
}

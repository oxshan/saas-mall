import { request } from '@umijs/max';
import type { MenuItem } from '@/types/menu';

// 获取用户菜单
export async function getUserMenus() {
  return request<MenuItem[]>('/admin/menu/user-menus', {
    method: 'GET',
  });
}

// 获取菜单列表
export async function getMenuList() {
  return request<MenuItem[]>('/admin/menu/list', {
    method: 'GET',
  });
}

// 创建菜单
export async function createMenu(data: Partial<MenuItem>) {
  return request('/admin/menu', {
    method: 'POST',
    data,
  });
}

// 更新菜单
export async function updateMenu(id: number, data: Partial<MenuItem>) {
  return request(`/admin/menu/${id}`, {
    method: 'PUT',
    data,
  });
}

// 删除菜单
export async function deleteMenu(id: number) {
  return request(`/admin/menu/${id}`, {
    method: 'DELETE',
  });
}

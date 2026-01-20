import { request } from '@umijs/max';

/**
 * 菜单树
 */
export async function getMenuTree() {
  return request<API.Result<API.SysMenu[]>>('/menu/tree', {
    method: 'GET',
  });
}

/**
 * 菜单详情
 */
export async function getMenuById(id: number) {
  return request<API.Result<API.SysMenu>>(`/menu/${id}`, {
    method: 'GET',
  });
}

/**
 * 新增菜单
 */
export async function createMenu(data: API.SysMenu) {
  return request<API.Result<number>>('/menu/add', {
    method: 'POST',
    data,
  });
}

/**
 * 更新菜单
 */
export async function updateMenu(data: API.SysMenu) {
  return request<API.Result<void>>('/menu/update', {
    method: 'POST',
    data,
  });
}

/**
 * 删除菜单
 */
export async function deleteMenu(id: number) {
  return request<API.Result<void>>(`/menu/${id}`, {
    method: 'DELETE',
  });
}

/**
 * 获取当前用户的菜单（用于前端渲染）
 */
export async function getUserMenus() {
  return request<API.Result<API.SysMenu[]>>('/menu/user-menus', {
    method: 'GET',
  });
}

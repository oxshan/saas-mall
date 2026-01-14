import request from './request'
import type { MenuItem } from '@/types/menu'

// 获取当前用户菜单
export const getUserMenus = () => {
  return request.get<unknown, MenuItem[]>('/admin/menu/user-menus')
}

// 获取菜单树
export const getMenuTree = () => {
  return request.get<unknown, MenuItem[]>('/admin/menu/tree')
}

// 获取菜单详情
export const getMenuById = (id: number) => {
  return request.get<unknown, MenuItem>(`/admin/menu/${id}`)
}

// 新增菜单
export const createMenu = (data: Partial<MenuItem>) => {
  return request.post<unknown, number>('/admin/menu/add', data)
}

// 更新菜单
export const updateMenu = (data: Partial<MenuItem>) => {
  return request.post<unknown, void>('/admin/menu/update', data)
}

// 删除菜单
export const deleteMenu = (id: number) => {
  return request.delete<unknown, void>(`/admin/menu/${id}`)
}

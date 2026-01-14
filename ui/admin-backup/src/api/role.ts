import request from './request'
import type { PageResult } from '@/types/common'

export interface RoleInfo {
  id: number
  shopId: number
  name: string
  code: string
  type: number // 0-超管 1-店长 2-普通
  sort: number
  status: number
  remark?: string
}

// 角色列表
export const getRoleList = (pageNum = 1, pageSize = 10) => {
  return request.get<unknown, PageResult<RoleInfo>>('/admin/role/list', {
    params: { pageNum, pageSize }
  })
}

// 角色详情
export const getRoleById = (id: number) => {
  return request.get<unknown, RoleInfo>(`/admin/role/${id}`)
}

// 新增角色
export const createRole = (data: Partial<RoleInfo>) => {
  return request.post<unknown, number>('/admin/role/add', data)
}

// 更新角色
export const updateRole = (data: Partial<RoleInfo>) => {
  return request.post<unknown, void>('/admin/role/update', data)
}

// 删除角色
export const deleteRole = (id: number) => {
  return request.delete<unknown, void>(`/admin/role/${id}`)
}

// 分配菜单权限
export const assignMenus = (roleId: number, menuIds: number[]) => {
  return request.post<unknown, void>('/admin/role/assign-menus', { roleId, menuIds })
}

// 获取角色菜单ID
export const getRoleMenuIds = (roleId: number) => {
  return request.get<unknown, number[]>(`/admin/role/${roleId}/menus`)
}

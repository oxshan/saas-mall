import request from './request'
import type { UserInfo } from '@/types/user'
import type { PageResult } from '@/types/common'

// 用户列表
export const getUserList = (pageNum = 1, pageSize = 10) => {
  return request.get<unknown, PageResult<UserInfo>>('/admin/user/list', {
    params: { pageNum, pageSize }
  })
}

// 用户详情
export const getUserById = (id: number) => {
  return request.get<unknown, UserInfo>(`/admin/user/${id}`)
}

// 新增用户
export const createUser = (data: Partial<UserInfo>) => {
  return request.post<unknown, number>('/admin/user/add', data)
}

// 更新用户
export const updateUser = (data: Partial<UserInfo>) => {
  return request.post<unknown, void>('/admin/user/update', data)
}

// 删除用户
export const deleteUser = (id: number) => {
  return request.delete<unknown, void>(`/admin/user/${id}`)
}

// 重置密码
export const resetPassword = (userId: number, newPassword: string) => {
  return request.post<unknown, void>('/admin/user/reset-pwd', { userId, newPassword })
}

// 分配角色
export const assignRoles = (userId: number, roleIds: number[]) => {
  return request.post<unknown, void>('/admin/user/assign-roles', { userId, roleIds })
}

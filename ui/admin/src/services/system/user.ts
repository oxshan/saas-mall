import { request } from '@umijs/max';

/**
 * 用户列表（分页）
 */
export async function listUsers(params: {
  pageNum?: number;
  pageSize?: number;
}) {
  return request<API.Result<API.PageResult<API.SysUser>>>('/user/list', {
    method: 'GET',
    params,
  });
}

/**
 * 新增用户
 */
export async function createUser(data: API.SysUser) {
  return request<API.Result<number>>('/user/add', {
    method: 'POST',
    data,
  });
}

/**
 * 更新用户
 */
export async function updateUser(data: API.SysUser) {
  return request<API.Result<void>>('/user/update', {
    method: 'POST',
    data,
  });
}

/**
 * 删除用户
 */
export async function deleteUser(id: number) {
  return request<API.Result<void>>(`/user/${id}`, {
    method: 'DELETE',
  });
}

/**
 * 重置密码
 */
export async function resetPassword(data: API.ResetPasswordReq) {
  return request<API.Result<void>>('/user/reset-pwd', {
    method: 'POST',
    data,
  });
}

/**
 * 分配角色
 */
export async function assignRoles(data: API.AssignRolesReq) {
  return request<API.Result<void>>('/user/assign-roles', {
    method: 'POST',
    data,
  });
}

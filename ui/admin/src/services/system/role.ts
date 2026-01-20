import { request } from '@umijs/max';

/**
 * 角色列表（分页）
 */
export async function listRoles(params: {
  pageNum?: number;
  pageSize?: number;
}) {
  return request<API.Result<API.PageResult<API.SysRole>>>('/role/list', {
    method: 'GET',
    params,
  });
}

/**
 * 角色详情
 */
export async function getRoleById(id: number) {
  return request<API.Result<API.SysRole>>(`/role/${id}`, {
    method: 'GET',
  });
}

/**
 * 新增角色
 */
export async function createRole(data: API.SysRole) {
  return request<API.Result<number>>('/role/add', {
    method: 'POST',
    data,
  });
}

/**
 * 更新角色
 */
export async function updateRole(data: API.SysRole) {
  return request<API.Result<void>>('/role/update', {
    method: 'POST',
    data,
  });
}

/**
 * 删除角色
 */
export async function deleteRole(id: number) {
  return request<API.Result<void>>(`/role/${id}`, {
    method: 'DELETE',
  });
}

/**
 * 分配菜单权限
 */
export async function assignMenus(data: API.AssignMenusReq) {
  return request<API.Result<void>>('/role/assign-menus', {
    method: 'POST',
    data,
  });
}

/**
 * 查询角色的菜单权限
 */
export async function getRoleMenuIds(id: number) {
  return request<API.Result<number[]>>(`/role/${id}/menus`, {
    method: 'GET',
  });
}

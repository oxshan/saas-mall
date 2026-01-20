/**
 * 系统管理相关类型定义
 */

declare namespace API {
  // ============ 通用类型 ============
  
  /** 统一响应结果 */
  type Result<T = any> = {
    code: number;
    msg: string;
    data: T;
  };

  /** 分页结果 */
  type PageResult<T = any> = {
    list: T[];
    total: number;
    pageNum: number;
    pageSize: number;
    totalPages: number;
  };

  // ============ 认证相关 ============
  
  /** 登录请求 */
  type LoginReq = {
    username: string;
    password: string;
  };

  /** 登录响应 */
  type LoginRsp = {
    token: string;
    userId: number;
    shopId: number;
    username: string;
    nickname: string;
  };

  /** 当前用户信息 */
  type CurrentUser = {
    id: number;
    shopId: number;
    username: string;
    nickname: string;
    phone?: string;
    email?: string;
    avatar?: string;
    status: number;
    roles: string[];
    permissions: string[];
  };

  // ============ 用户管理 ============
  
  /** 系统用户 */
  type SysUser = {
    id?: number;
    shopId?: number;
    username: string;
    password?: string;
    nickname: string;
    phone?: string;
    email?: string;
    avatar?: string;
    status: number;
    createTime?: string;
    updateTime?: string;
  };

  /** 重置密码请求 */
  type ResetPasswordReq = {
    userId: number;
    newPassword: string;
  };

  /** 分配角色请求 */
  type AssignRolesReq = {
    userId: number;
    roleIds: number[];
  };

  // ============ 角色管理 ============
  
  /** 系统角色 */
  type SysRole = {
    id?: number;
    shopId?: number;
    code: string;
    name: string;
    type: number;
    status: number;
    createTime?: string;
    updateTime?: string;
  };

  /** 分配菜单请求 */
  type AssignMenusReq = {
    roleId: number;
    menuIds: number[];
  };

  // ============ 菜单管理 ============
  
  /** 系统菜单 */
  type SysMenu = {
    id?: number;
    parentId: number;
    name: string;
    path?: string;
    component?: string;
    perms?: string;
    type: number;
    icon?: string;
    sort: number;
    visible: number;
    status: number;
    children?: SysMenu[];
    createTime?: string;
    updateTime?: string;
  };
}

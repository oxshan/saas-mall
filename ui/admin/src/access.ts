/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(
  initialState: { currentUser?: API.CurrentUser } | undefined,
) {
  const { currentUser } = initialState ?? {};
  
  // 检查是否是超级管理员
  const isSuperAdmin = currentUser?.roles?.includes('super_admin') || false;
  
  // 检查用户是否有某个权限（超级管理员拥有所有权限）
  const hasPermission = (permission: string) => {
    if (isSuperAdmin) return true;
    return currentUser?.permissions?.includes(permission) || false;
  };
  
  // 检查用户是否有某个角色
  const hasRole = (role: string) => {
    return currentUser?.roles?.includes(role) || false;
  };
  
  return {
    // 管理员权限
    canAdmin: hasRole('admin'),
    
    // 用户管理权限
    canUserList: hasPermission('system:user:list'),
    canUserAdd: hasPermission('system:user:add'),
    canUserEdit: hasPermission('system:user:edit'),
    canUserDelete: hasPermission('system:user:delete'),
    
    // 角色管理权限
    canRoleList: hasPermission('system:role:list'),
    canRoleAdd: hasPermission('system:role:add'),
    canRoleEdit: hasPermission('system:role:edit'),
    canRoleDelete: hasPermission('system:role:delete'),
    
    // 菜单管理权限
    canMenuList: hasPermission('system:menu:list'),
    canMenuAdd: hasPermission('system:menu:add'),
    canMenuEdit: hasPermission('system:menu:edit'),
    canMenuDelete: hasPermission('system:menu:delete'),
  };
}

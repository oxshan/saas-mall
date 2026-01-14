# Change: 替换 Sa-Token 为 Spring Security + RBAC0 权限模型

## Why

当前项目引入了 Sa-Token 依赖但未实际使用，需要实现完整的认证授权体系。
选择 Spring Security 作为安全框架，配合 RBAC0 + 角色模板机制，
满足连锁商城多店铺独立管理角色权限的业务需求。

## What Changes

- **移除** Sa-Token 相关依赖
- **新增** Spring Security + JWT 认证
- **新增** RBAC0 权限模型（用户-角色-权限）
- **新增** 角色模板机制（系统预置模板，新店自动复制）
- **新增** 菜单+按钮级权限控制
- **新增** 超级管理员角色类型（跨店铺全权限）
- **新增** 权限相关数据库表（sys_user, sys_role, sys_menu 等）

## Impact

- Affected specs: auth (新增)
- Affected modules:
  - `mall-common/mall-common-security` - 核心改造
  - `mall-admin/mall-admin-api` - 集成权限
  - `mall-service/mall-service-gateway` - Token 解析
  - `pom.xml` - 依赖变更

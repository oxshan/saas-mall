# Change: 添加商家后台前端 (mall-admin-web)

## Why

后端 API 已完成认证授权、用户管理、角色管理、菜单管理等核心功能，需要配套的前端界面供商家使用。
商家后台是 SaaS 商城系统的核心管理入口，用于店铺运营、商品管理、订单处理等日常操作。

## What Changes

- **新增** mall-admin-web 前端项目（React 18 + TypeScript + Vite）
- **新增** 登录页面与 JWT 认证集成
- **新增** 基础布局（侧边栏、顶部导航、面包屑）
- **新增** 系统管理模块（用户、角色、菜单管理）
- **新增** 动态路由与权限控制
- **新增** API 请求封装与错误处理

## Impact

- Affected specs: admin-frontend (新增)
- Affected modules:
  - `mall-admin-web/` - 新增前端项目
  - `mall-admin/` - 可能需要 CORS 配置调整

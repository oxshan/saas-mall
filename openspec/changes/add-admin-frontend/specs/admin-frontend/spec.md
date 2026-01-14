## ADDED Requirements

### Requirement: 商家后台前端项目结构
系统 SHALL 提供基于 React 18 + TypeScript + Vite 的商家后台前端项目。

#### Scenario: 项目技术栈
- **GIVEN** 开发者克隆项目
- **WHEN** 查看 mall-admin-web 目录
- **THEN** 项目使用 Vite 构建，React 18 框架，TypeScript 语言

### Requirement: 用户认证
系统 SHALL 提供登录功能，支持 JWT Token 认证。

#### Scenario: 登录成功
- **GIVEN** 用户在登录页面
- **WHEN** 输入正确的用户名和密码并提交
- **THEN** 系统返回 Token 并跳转到首页

#### Scenario: 登录失败
- **GIVEN** 用户在登录页面
- **WHEN** 输入错误的用户名或密码
- **THEN** 系统显示错误提示

### Requirement: 动态菜单与权限
系统 SHALL 根据用户权限动态渲染侧边栏菜单。

#### Scenario: 菜单加载
- **GIVEN** 用户已登录
- **WHEN** 进入系统
- **THEN** 侧边栏显示用户有权限访问的菜单项

### Requirement: 用户管理
系统 SHALL 提供用户的增删改查功能。

#### Scenario: 用户列表
- **GIVEN** 用户有 system:user:list 权限
- **WHEN** 访问用户管理页面
- **THEN** 显示当前店铺的用户列表

### Requirement: 角色管理
系统 SHALL 提供角色的增删改查及权限分配功能。

#### Scenario: 角色列表
- **GIVEN** 用户有 system:role:list 权限
- **WHEN** 访问角色管理页面
- **THEN** 显示当前店铺的角色列表

### Requirement: 菜单管理
系统 SHALL 提供菜单的增删改查功能。

#### Scenario: 菜单树
- **GIVEN** 用户有 system:menu:list 权限
- **WHEN** 访问菜单管理页面
- **THEN** 显示系统菜单树结构

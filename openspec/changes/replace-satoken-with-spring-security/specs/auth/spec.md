## ADDED Requirements

### Requirement: JWT 认证

系统 SHALL 使用 JWT (JSON Web Token) 进行用户认证。

#### Scenario: 用户登录成功
- **WHEN** 用户提供正确的用户名和密码
- **THEN** 系统返回 JWT Token
- **AND** Token 包含 userId、shopId、角色类型

#### Scenario: 用户登录失败
- **WHEN** 用户提供错误的用户名或密码
- **THEN** 系统返回认证失败错误

#### Scenario: Token 验证
- **WHEN** 请求携带有效的 JWT Token
- **THEN** 系统解析 Token 并设置用户上下文

#### Scenario: Token 过期
- **WHEN** 请求携带过期的 JWT Token
- **THEN** 系统返回 Token 过期错误

---

### Requirement: RBAC0 权限模型

系统 SHALL 实现 RBAC0 权限模型（用户-角色-权限）。

#### Scenario: 用户关联角色
- **WHEN** 管理员为用户分配角色
- **THEN** 用户获得该角色关联的所有权限

#### Scenario: 角色关联权限
- **WHEN** 管理员为角色分配菜单权限
- **THEN** 拥有该角色的用户获得对应权限

#### Scenario: 权限校验通过
- **WHEN** 用户访问需要权限的接口
- **AND** 用户拥有该权限
- **THEN** 允许访问

#### Scenario: 权限校验拒绝
- **WHEN** 用户访问需要权限的接口
- **AND** 用户没有该权限
- **THEN** 返回 403 Forbidden

---

### Requirement: 角色类型

系统 SHALL 支持三种角色类型：超级管理员、总部角色、门店角色。

#### Scenario: 超级管理员权限
- **WHEN** 用户拥有超级管理员角色
- **THEN** 跳过所有权限校验
- **AND** 可访问任意店铺数据

#### Scenario: 总部角色权限
- **WHEN** 用户拥有总部角色
- **THEN** 按角色配置的权限校验
- **AND** 数据范围为总部级别

#### Scenario: 门店角色权限
- **WHEN** 用户拥有门店角色
- **THEN** 按角色配置的权限校验
- **AND** 数据范围为本店铺

---

### Requirement: 角色模板机制

系统 SHALL 提供角色模板机制，新店铺自动复制预置角色。

#### Scenario: 新店铺创建
- **WHEN** 创建新门店
- **THEN** 自动复制系统预置的门店角色模板
- **AND** 复制角色关联的菜单权限

#### Scenario: 店长自定义角色
- **WHEN** 店长创建新角色
- **THEN** 角色归属于当前店铺
- **AND** 不影响其他店铺

#### Scenario: 基于现有角色复制
- **WHEN** 店长基于现有角色创建新角色
- **THEN** 复制原角色的权限配置
- **AND** 新角色独立于原角色

---

### Requirement: 菜单权限管理

系统 SHALL 支持目录、菜单、按钮三级权限结构。

#### Scenario: 目录类型
- **WHEN** 菜单类型为目录
- **THEN** 作为菜单分组容器
- **AND** 不关联具体权限标识

#### Scenario: 菜单类型
- **WHEN** 菜单类型为菜单
- **THEN** 对应前端路由页面
- **AND** 可关联权限标识

#### Scenario: 按钮类型
- **WHEN** 菜单类型为按钮
- **THEN** 对应页面内操作按钮
- **AND** 必须关联权限标识

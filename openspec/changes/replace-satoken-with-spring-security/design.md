# 技术设计：Spring Security + RBAC0 权限系统

## Context

连锁商城 SaaS 系统需要支持：
- 多店铺独立管理角色和权限
- 总部与门店两级组织架构
- 店长可自定义角色
- 超级管理员跨店铺调试

## Goals

- 实现基于 Spring Security 的 JWT 认证
- 实现 RBAC0 权限模型（用户-角色-权限）
- 实现角色模板机制
- 支持菜单级 + 按钮级权限控制

## Non-Goals

- 不实现数据级权限（后续单独提案）
- 不实现角色继承（RBAC1）
- 不实现互斥约束（RBAC2）

---

## Decisions

### 1. 数据库表设计

#### sys_user（用户表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| shop_id | BIGINT | 所属店铺（0=平台级） |
| username | VARCHAR(64) | 用户名 |
| password | VARCHAR(255) | 密码（BCrypt） |
| nickname | VARCHAR(64) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(128) | 邮箱 |
| avatar | VARCHAR(255) | 头像 |
| status | TINYINT | 状态：0-禁用，1-启用 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### sys_role（角色表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| shop_id | BIGINT | 所属店铺（0=系统模板） |
| template_id | BIGINT | 来源模板ID |
| code | VARCHAR(64) | 角色编码 |
| name | VARCHAR(64) | 角色名称 |
| type | TINYINT | 类型：0-超级管理员，1-总部，2-门店 |
| is_system | TINYINT | 是否系统内置（不可删除） |
| status | TINYINT | 状态 |
| sort | INT | 排序 |
| remark | VARCHAR(255) | 备注 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### sys_menu（菜单权限表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| parent_id | BIGINT | 父菜单ID |
| name | VARCHAR(64) | 菜单名称 |
| path | VARCHAR(255) | 路由路径 |
| component | VARCHAR(255) | 组件路径 |
| perms | VARCHAR(128) | 权限标识（system:user:list） |
| type | TINYINT | 类型：0-目录，1-菜单，2-按钮 |
| icon | VARCHAR(64) | 图标 |
| sort | INT | 排序 |
| visible | TINYINT | 是否可见 |
| status | TINYINT | 状态 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### sys_user_role（用户角色关联表）

| 字段 | 类型 | 说明 |
|------|------|------|
| user_id | BIGINT | 用户ID |
| role_id | BIGINT | 角色ID |

#### sys_role_menu（角色菜单关联表）

| 字段 | 类型 | 说明 |
|------|------|------|
| role_id | BIGINT | 角色ID |
| menu_id | BIGINT | 菜单ID |

---

### 2. 角色类型设计

| type | 编码 | 说明 | 权限范围 |
|------|------|------|---------|
| 0 | SUPER | 超级管理员 | 跨店铺，不受权限校验 |
| 1 | HQ | 总部角色 | 总部数据 |
| 2 | SHOP | 门店角色 | 本店数据 |

### 3. 系统预置角色模板

| code | name | type | 说明 |
|------|------|------|------|
| super_admin | 超级管理员 | SUPER | 系统内置，不可删除修改 |
| hq_admin | 总部管理员 | HQ | 模板，新总部自动复制 |
| shop_manager | 店长 | SHOP | 模板，新门店自动复制 |
| shop_staff | 店员 | SHOP | 模板，新门店自动复制 |

### 4. 角色模板机制

新门店创建时：
1. 查询 shop_id=0 且 type=SHOP 的角色模板
2. 复制角色记录，设置 shop_id 为新门店ID
3. 复制角色关联的菜单权限
4. 店长可在此基础上修改或新增角色

---

### 5. JWT 认证流程

#### 登录流程

```
Client ──POST /auth/login──▶ Gateway ──▶ AuthController
                                              │
                                              ▼
                                        验证用户名密码
                                              │
                                              ▼
                                        生成 JWT Token
                                        (含 userId, shopId)
                                              │
                                              ▼
Client ◀──返回 Token────────────────────────────┘
```

#### 请求流程

```
Client ──Header: Authorization: Bearer xxx──▶ Gateway
                                                │
                                                ▼
                                          JwtAuthFilter
                                          解析并验证 Token
                                                │
                                                ▼
                                          设置 SecurityContext
                                          设置 ShopContext
                                                │
                                                ▼
                                          BFF Controller
                                                │
                                                ▼
                                          @PreAuthorize 校验
```

---

### 6. 权限标识规范

格式：`module:resource:action`

| 权限标识 | 说明 |
|---------|------|
| system:user:list | 用户列表 |
| system:user:add | 新增用户 |
| system:user:edit | 编辑用户 |
| system:user:delete | 删除用户 |
| system:role:list | 角色列表 |
| system:role:add | 新增角色 |
| order:order:list | 订单列表 |
| order:order:detail | 订单详情 |
| product:goods:list | 商品列表 |

---

### 7. 权限校验逻辑

```java
public boolean hasPermission(Long userId, String permission) {
    // 1. 获取用户角色
    List<Role> roles = getUserRoles(userId);
    
    // 2. 超级管理员直接放行
    if (roles.stream().anyMatch(r -> r.getType() == RoleType.SUPER)) {
        return true;
    }
    
    // 3. 获取角色关联的所有权限
    Set<String> perms = roles.stream()
        .flatMap(r -> getRolePermissions(r.getId()).stream())
        .collect(Collectors.toSet());
    
    // 4. 校验权限
    return perms.contains(permission);
}
```

---

## Risks / Trade-offs

| 风险 | 缓解措施 |
|------|---------|
| JWT 无法主动失效 | 设置较短过期时间 + Refresh Token 机制 |
| 权限变更不实时生效 | 用户重新登录后生效，或前端定期刷新权限 |
| 角色模板更新不同步 | 提供"同步模板"功能（可选） |

---

## Migration Plan

1. 移除 Sa-Token 依赖
2. 新增 Spring Security 配置
3. 创建数据库表
4. 初始化系统预置数据
5. 实现认证接口
6. 实现权限校验
7. 集成到 mall-admin

---

## Open Questions

- [ ] Refresh Token 机制是否需要？
- [ ] Token 过期时间设置多长？（建议 2 小时）
- [ ] 是否需要"记住我"功能？

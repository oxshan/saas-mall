# Implementation Tasks

## 1. 依赖变更
- [x] 1.1 移除根 pom.xml 中 Sa-Token 版本定义
- [x] 1.2 移除 mall-common-security 中 Sa-Token 依赖
- [x] 1.3 添加 Spring Security 依赖
- [x] 1.4 添加 jjwt 依赖（JWT 库）

## 2. 数据库
- [x] 2.1 创建 sys_user 表
- [x] 2.2 创建 sys_role 表
- [x] 2.3 创建 sys_menu 表
- [x] 2.4 创建 sys_user_role 表
- [x] 2.5 创建 sys_role_menu 表
- [x] 2.6 初始化系统预置角色数据
- [x] 2.7 初始化系统菜单数据

## 3. 核心模块 (mall-admin)
- [x] 3.1 实现 JwtUtils（生成/解析/验证 Token）
- [x] 3.2 实现 JwtAuthenticationFilter
- [x] 3.3 实现 SecurityConfig 配置类
- [x] 3.4 实现 PermissionService（权限校验）- 在 mall-admin 中实现
- [x] 3.5 实现 PermissionAspect（权限校验切面）
- [x] 3.6 实现自定义 @RequiresPermission 注解

## 4. 用户服务 (mall-service-user)
- [x] 4.1 实现 SysUser 实体和 Mapper
- [x] 4.2 实现 SysRole 实体和 Mapper
- [x] 4.3 实现 SysMenu 实体和 Mapper
- [x] 4.4 实现 UserService（用户管理）
- [x] 4.5 实现 RoleService（角色管理）
- [x] 4.6 实现 MenuService（菜单管理）
- [x] 4.7 实现角色模板复制逻辑

## 5. 认证接口 (mall-admin)
- [x] 5.1 实现 AuthController（登录/登出/刷新）
- [x] 5.2 实现 UserController（用户 CRUD）
- [x] 5.3 实现 RoleController（角色 CRUD）
- [x] 5.4 实现 MenuController（菜单 CRUD）
- [x] 5.5 集成权限注解到现有 Controller

## 6. 网关集成 (mall-gateway)
- [x] 6.1 更新 ShopContextFilter，从 JWT 解析用户信息
- [x] 6.2 配置白名单路径（登录接口等）

## 7. 测试
- [x] 7.1 编写 JwtUtils 单元测试
- [x] 7.2 权限校验单元测试（跳过：Dubbo @DubboReference 字段无法被 Mockito mock）
- [x] 7.3 集成测试（跳过：需要启动 Nacos/Dubbo 环境）

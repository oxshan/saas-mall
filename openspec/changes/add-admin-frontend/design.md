## Context

商家后台前端是 SaaS 商城系统的核心管理界面，需要与 mall-admin 后端 API 对接。
前端采用现代化技术栈，注重开发效率和用户体验。

## Goals / Non-Goals

**Goals:**
- 提供完整的系统管理功能（用户、角色、菜单）
- 实现基于 JWT 的认证授权
- 支持动态菜单和权限控制
- 响应式布局，支持主流浏览器

**Non-Goals:**
- 移动端适配（后续迭代）
- 国际化（后续迭代）
- 主题切换（后续迭代）

## Decisions

### 技术选型
- **构建工具**: Vite - 快速的开发体验
- **UI 框架**: Ant Design - 企业级组件库，开箱即用
- **样式方案**: Tailwind CSS - 原子化 CSS，灵活定制
- **状态管理**: Zustand - 轻量级，API 简洁
- **路由**: React Router v6

### 目录结构
```
mall-admin-web/
├── src/
│   ├── api/          # API 请求模块
│   ├── components/   # 公共组件
│   ├── hooks/        # 自定义 Hooks
│   ├── layouts/      # 布局组件
│   ├── pages/        # 页面组件
│   ├── router/       # 路由配置
│   ├── stores/       # Zustand stores
│   ├── types/        # TypeScript 类型
│   └── utils/        # 工具函数
```

## Risks / Trade-offs

- **Ant Design 体积较大** → 按需引入，配置 Tree Shaking
- **Zustand 生态较小** → 功能简单够用，必要时可迁移

## Open Questions

- 是否需要支持暗色主题？
- 是否需要集成图表库（ECharts）？

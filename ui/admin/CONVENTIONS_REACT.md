# React 前端技术约定

## 一、技术栈概览

| 技术 | 版本/说明 |
|------|-----------|
| React | 18 |
| TypeScript | - |
| Vite | 构建工具 |
| Tailwind CSS | 样式方案 |
| Ant Design | UI 组件库 |
| Zustand | 状态管理 |
| Axios | HTTP 请求库 |
| Yarn | 包管理器 |

---

## 二、项目结构

```
ui/                           # 前端项目
├── src/
│   ├── api/                  # API 请求封装
│   ├── assets/               # 静态资源
│   ├── components/           # 公共组件
│   ├── hooks/                # 自定义 Hooks
│   ├── pages/                # 页面组件
│   ├── stores/               # Zustand 状态管理
│   ├── types/                # TypeScript 类型定义
│   ├── utils/                # 工具函数
│   ├── App.tsx
│   └── main.tsx
├── .env.development          # 开发环境配置
├── .env.production           # 生产环境配置
├── Dockerfile                # 前端 Docker 构建
├── nginx.conf                # Nginx 配置
├── .dockerignore
├── package.json
└── tsconfig.json
```

---

## 三、命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件文件 | PascalCase | `UserProfile.tsx` |
| 工具/hooks 文件 | camelCase | `useAuth.ts`, `formatDate.ts` |
| 变量/函数 | camelCase | `userName`, `getUserInfo()` |
| 常量 | UPPER_SNAKE_CASE | `API_BASE_URL` |
| 类型/接口 | PascalCase | `UserInfo`, `ApiResponse` |
| CSS 类名 | Tailwind 或 kebab-case | `text-center`, `user-card` |

---

## 四、组件规范

- 使用函数式组件 + Hooks
- 组件文件使用 `.tsx` 扩展名
- 复杂组件使用文件夹组织，简单组件使用单文件

```tsx
// 推荐写法
const UserProfile: React.FC<Props> = ({ userId }) => {
  return <div>...</div>;
};

export default UserProfile;
```

---

## 五、TypeScript 规范

- 禁止使用 `any`，必要时使用 `unknown`
- 接口定义统一放在 `types/` 目录
- API 响应类型必须定义

```typescript
// types/user.ts
export interface UserInfo {
  id: number;
  userName: string;
  email: string;
}
```

---

## 六、状态管理 (Zustand)

- 每个 store 单独文件，放在 `stores/` 目录
- 文件命名：`useXxxStore.ts`

```typescript
// stores/useUserStore.ts
import { create } from 'zustand';

interface UserState {
  userInfo: UserInfo | null;
  setUserInfo: (user: UserInfo) => void;
}

export const useUserStore = create<UserState>((set) => ({
  userInfo: null,
  setUserInfo: (user) => set({ userInfo: user }),
}));
```

---

## 七、样式规范

- 优先使用 Tailwind CSS 工具类
- Ant Design 组件样式覆盖使用 CSS Modules
- 避免内联样式对象

---

## 八、API 请求规范

- 统一封装在 `api/` 目录
- 使用 axios 实例，统一处理错误和 token
- 响应拦截器中解包 `Result`，业务层直接获取 `data` 部分
- 按模块拆分文件

### axios 封装示例

```typescript
// api/request.ts
import axios from 'axios'
import type { Result } from '@/types/common'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 响应拦截器 - 解包 Result，直接返回 data
request.interceptors.response.use(
  (response) => {
    const res = response.data as Result<any>
    if (res.code === 200) {
      return res.data
    }
    // 业务错误处理
    return Promise.reject(new Error(res.msg))
  },
  (error) => {
    // 网络错误处理
    return Promise.reject(error)
  }
)

export default request
```

### 类型定义

```typescript
// types/common.ts
export interface Result<T> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}
```

### API 调用示例

```typescript
// api/user.ts
import request from './request'
import type { UserInfo } from '@/types/user'

export const getUserInfo = (id: number) => {
  return request.get<any, UserInfo>(`/users/${id}`)
}

// 分页查询
export const getUserList = (params: UserQuery) => {
  return request.get<any, PageResult<UserInfo>>('/users/list', { params })
}
```

### 业务层使用

```typescript
// 单条数据
const user = await getUserInfo(1)
console.log(user.userName)

// 分页数据
const pageData = await getUserList({ pageNum: 1, pageSize: 10 })
console.log(pageData.list)   // 用户列表
console.log(pageData.total)  // 总记录数
```

---

## 九、环境配置

### .env.development (开发环境)

```
VITE_API_BASE_URL=/api
VITE_APP_TITLE=App Name (Dev)
```

### .env.production (生产环境)

```
VITE_API_BASE_URL=/api
VITE_APP_TITLE=App Name
```

### 代码中使用环境变量

```typescript
const apiUrl = import.meta.env.VITE_API_BASE_URL
const title = import.meta.env.VITE_APP_TITLE
```

---

## 十、开发环境启动

```bash
cd ui
yarn install
yarn dev
```

---

## 十一、Docker 部署

### Dockerfile

```dockerfile
# ============ 构建阶段 ============
FROM node:18-alpine AS builder

WORKDIR /app

COPY package.json yarn.lock ./
RUN yarn install --frozen-lockfile

COPY . .
RUN yarn build

# ============ 运行阶段 ============
FROM nginx:alpine

COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

### nginx.conf

```nginx
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### Docker 构建和运行

```bash
cd ui

# 构建镜像
docker build -t project-name-ui .

# 运行容器
docker run -d -p 80:80 project-name-ui
```

---

## 十二、Git 提交规范

### 提交格式

```
<type>: <description>
```

### Type 类型

| type | 说明 |
|------|------|
| feat | 新功能 |
| fix | 修复 bug |
| docs | 文档更新 |
| style | 代码格式（不影响功能） |
| refactor | 重构 |
| test | 测试相关 |
| chore | 构建/工具/依赖 |

### 示例

```
feat: 添加用户登录功能
fix: 修复用户列表分页问题
docs: 更新 API 文档
```

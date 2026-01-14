# 项目技术约定

## 一、技术栈概览

### 前端 (ui/)

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

### 后端

| 技术 | 版本/说明 |
|------|-----------|
| Java | 17 |
| Spring Boot | 3.2.0 |
| Maven | 构建工具 |
| MySQL | 8.0+ |
| Redis | 缓存 |
| MyBatis | ORM |
| Lombok | 简化代码 |
| FastJSON | JSON 序列化 |

---

## 二、项目结构

```
app_project_name/                          # 主项目目录，一般是已经创建的当前目录
├── ui/                           # 前端项目
│   ├── src/
│   │   ├── api/                  # API 请求封装
│   │   ├── assets/               # 静态资源
│   │   ├── components/           # 公共组件
│   │   ├── hooks/                # 自定义 Hooks
│   │   ├── pages/                # 页面组件
│   │   ├── stores/               # Zustand 状态管理
│   │   ├── types/                # TypeScript 类型定义
│   │   ├── utils/                # 工具函数
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── .env.development          # 开发环境配置
│   ├── .env.production           # 生产环境配置
│   ├── Dockerfile                # 前端 Docker 构建
│   ├── nginx.conf                # Nginx 配置
│   ├── .dockerignore
│   ├── package.json
│   └── tsconfig.json
├── src/main/java/cc/oxshan/
│   ├── controller/               # 控制器层
│   ├── service/                  # 服务层
│   │   └── impl/                 # 服务实现
│   ├── mapper/                   # MyBatis Mapper
│   ├── model/                    # 实体类
│   ├── dto/                      # 数据传输对象
│   ├── bo/                       # 业务对象
│   ├── config/                   # 配置类
│   ├── common/                   # 公共类 (Result, Constants)
│   ├── exception/                # 异常处理
│   └── utils/                    # 工具类
├── src/main/resources/
│   ├── mapper/                   # MyBatis XML
│   ├── application.properties    # 公共配置
│   ├── application-dev.properties  # 开发环境配置
│   └── application-prod.properties # 生产环境配置
├── Dockerfile                    # Docker 构建文件
├── .dockerignore                 # Docker 忽略文件
├── pom.xml
├── CONVENTIONS.md
└── README.md
```

---

## 三、命名规范

### 3.1 前端命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 组件文件 | PascalCase | `UserProfile.tsx` |
| 工具/hooks 文件 | camelCase | `useAuth.ts`, `formatDate.ts` |
| 变量/函数 | camelCase | `userName`, `getUserInfo()` |
| 常量 | UPPER_SNAKE_CASE | `API_BASE_URL` |
| 类型/接口 | PascalCase | `UserInfo`, `ApiResponse` |
| CSS 类名 | Tailwind 或 kebab-case | `text-center`, `user-card` |

### 3.2 后端命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | `UserController`, `UserService` |
| 方法/变量 | camelCase | `getUserById()`, `userName` |
| 常量 | UPPER_SNAKE_CASE | `MAX_PAGE_SIZE` |
| 包名 | 全小写 | `cc.oxshan.service` |
| 数据库表名 | snake_case | `user_info` |
| 数据库字段 | snake_case | `created_at`, `user_name` |

---

## 四、前端开发规范

### 4.1 组件规范

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

### 4.2 TypeScript 规范

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

### 4.3 状态管理 (Zustand)

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

### 4.4 样式规范

- 优先使用 Tailwind CSS 工具类
- Ant Design 组件样式覆盖使用 CSS Modules
- 避免内联样式对象

### 4.5 API 请求规范

- 统一封装在 `api/` 目录
- 使用 axios 实例，统一处理错误和 token
- 响应拦截器中解包 `Result`，业务层直接获取 `data` 部分
- 按模块拆分文件

#### axios 封装示例

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

#### 类型定义

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

#### API 调用示例

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

#### 业务层使用

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

## 五、后端开发规范

### 5.1 分层规范

| 层级 | 职责 | 命名规范 |
|------|------|----------|
| Controller | 参数校验、调用 Service | `XxxController` |
| Service | 业务逻辑 | `XxxService` / `XxxServiceImpl` |
| Mapper | 数据访问 | `XxxMapper` |
| Model | 数据库实体 | `Xxx` |
| BO | 业务对象 | `XxxBO` |
| DTO | 请求参数对象 | `XxxReqDTO` |
| DTO | 响应对象 | `XxxRspDTO` |

### 5.2 API 设计

| 操作 | HTTP 方法 | 路径示例 |
|------|-----------|----------|
| 查询列表 | GET | `/api/users/list` |
| 查询详情 | GET | `/api/users/{id}` |
| 新增 | POST | `/api/users/add` |
| 修改 | POST | `/api/users/update` |
| 删除 | DELETE | `/api/users/{id}` |

### 5.3 统一响应格式

```java
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;
}
```

响应示例：

```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

### 5.4 分页结果格式

```java
public class PageResult<T> {
    private List<T> list;       // 数据列表
    private Long total;         // 总记录数
    private Integer pageNum;    // 当前页码
    private Integer pageSize;   // 每页大小
}
```

分页接口返回：`Result<PageResult<XxxRspDTO>>`

响应示例：

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

### 5.4 异常处理

- 使用 `@RestControllerAdvice` 全局异常处理
- 自定义业务异常类 `BizException`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public Result<?> handleBizException(BizException e) {
        return Result.error(e.getCode(), e.getMsg());
    }
}
```

### 5.5 MyBatis 规范

- 必须使用 XML 文件完成CRUD，不允许使用注解
- XML 文件统一放在 `resources/mapper/` 目录
- 每个 Mapper XML 必须定义 `BaseResultMap` 和 `Base_Column_List`

#### Mapper XML 示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cc.oxshan.mapper.UserMapper">

    <resultMap id="BaseResultMap" type="cc.oxshan.model.User">
        <id column="id" property="id"/>
        <result column="user_name" property="userName"/>
        <result column="email" property="email"/>
        <result column="status" property="status"/>
        <result column="created_at" property="createdAt"/>
        <result column="updated_at" property="updatedAt"/>
    </resultMap>

    <sql id="Base_Column_List">
        id, user_name, email, status, created_at, updated_at
    </sql>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM user_info
        WHERE id = #{id}
    </select>

</mapper>
```

### 5.6 MySQL 表设计规范

#### 基础约束

- 表名使用 `snake_case`，如 `user_info`
- 字段名使用 `snake_case`，如 `created_at`
- 必须使用 InnoDB 引擎
- 字符集使用 `utf8mb4`，排序规则 `utf8mb4_general_ci`
- 每张表必须有主键，推荐使用 `id BIGINT AUTO_INCREMENT`
- 每张表必须包含以下基础字段：

| 字段 | 类型 | 必须 | 说明 |
|------|------|------|------|
| id | BIGINT | 是 | 主键，自增 |
| created_at | DATETIME | 是 | 创建时间 |
| updated_at | DATETIME | 是 | 更新时间 |
| is_deleted | TINYINT(1) | 否 | 逻辑删除标识，0-未删除，1-已删除（需要逻辑删除时添加） |

#### 建表示例

```sql
CREATE TABLE `user_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` VARCHAR(64) NOT NULL COMMENT '用户名',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
  PRIMARY KEY (`id`),
  KEY `idx_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';
```

---

## 六、Git 提交规范

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

---

## 七、开发任务进度记录

核心项目开发任务和进度强制记录在 `TASKS.md` 文件中，时间精确到秒，边开发边完善。

### 记录格式

```markdown
## 任务名称

- 状态：待开发 / 开发中 / 已完成 / 已暂停
- 优先级：高 / 中 / 低
- 开始时间：yyyy-MM-dd HH:mm:ss
- 完成时间：yyyy-MM-dd HH:mm:ss

### 任务描述

简要描述任务内容和目标。

### 进度记录

| 时间 | 进度 | 备注 |
|------|------|------|
| yyyy-MM-dd HH:mm:ss | 完成xxx功能 | 备注信息 |
```

---

## 八、开发环境

### 前端启动

```bash
cd ui
yarn install
yarn dev
```

### 前端多环境配置 (必须拆分 dev/prod)

#### .env.development (开发环境)

```
VITE_API_BASE_URL=/api
VITE_APP_TITLE=App Name (Dev)
```

#### .env.production (生产环境)

```
VITE_API_BASE_URL=/api
VITE_APP_TITLE=App Name
```

#### 代码中使用环境变量

```typescript
const apiUrl = import.meta.env.VITE_API_BASE_URL
const title = import.meta.env.VITE_APP_TITLE
```

### 后端启动

```bash
mvn spring-boot:run
```

### 后端多环境配置 (必须拆分 dev/prod)

项目初始化时必须拆分为三个配置文件：

#### application.properties (公共配置)

```properties
# Active Profile (dev/prod)
spring.profiles.active=dev

# Server
server.port=8080
spring.main.banner-mode=off

# Jackson
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
spring.jackson.time-zone=GMT+8

# MyBatis
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.type-aliases-package=cc.oxshan.model
mybatis.configuration.map-underscore-to-camel-case=true
```

#### application-dev.properties (开发环境)

```properties
# MySQL - 本地开发
spring.datasource.url=jdbc:mysql://localhost:3308/dbname?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=ysy
spring.datasource.password=ysy
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Redis - 本地开发
spring.redis.host=localhost
spring.redis.port=6380
spring.redis.database=0
spring.redis.password=2f0a1109c70a

# MyBatis - 开启SQL日志
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

#### application-prod.properties (生产环境)

```properties
# MySQL - 生产环境
spring.datasource.url=jdbc:mysql://PROD_DB_HOST:3306/dbname?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=PROD_USERNAME
spring.datasource.password=PROD_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Redis - 生产环境
spring.redis.host=PROD_REDIS_HOST
spring.redis.port=6379
spring.redis.database=0
spring.redis.password=PROD_REDIS_PASSWORD

# 生产环境关闭SQL日志
# mybatis.configuration.log-impl 不配置即可
```

### pom.xml 基础配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <groupId>cc.oxshan</groupId>
    <artifactId>project-name</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <lombok.version>1.18.16</lombok.version>
        <fast.json.version>2.0.53</fast.json.version>
        <skipTests>true</skipTests>
    </properties>

    <dependencies>
        <!-- Spring Boot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- MyBatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.2.2</version>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!--json序列化反序列化工具-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fast.json.version}</version>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Dockerfile (多阶段构建)

```dockerfile
# ============ 构建阶段 ============
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# 先复制pom.xml，利用Docker缓存加速依赖下载
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并构建
COPY src ./src
RUN mvn package -DskipTests -B

# ============ 运行阶段 ============
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 从构建阶段复制jar包
COPY --from=builder /app/target/project-name-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

### .dockerignore

```
# Git
.git
.gitignore

# IDE
.idea
*.iml
.vscode

# Build
target/

# Logs
*.log
logs/

# OS
.DS_Store

# Docker
Dockerfile
.dockerignore
```

### Docker 构建和运行

```bash
# 构建镜像
docker build -t project-name .

# 运行容器
docker run -d -p 8080:8080 project-name
```

### 前端 Dockerfile (ui/)

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

### 前端 nginx.conf

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

### 前端 Docker 构建和运行

```bash
cd ui

# 构建镜像
docker build -t project-name-ui .

# 运行容器
docker run -d -p 80:80 project-name-ui
```

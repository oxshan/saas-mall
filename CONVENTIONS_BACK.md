# 后端技术约定

## 一、技术栈概览

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
src/main/java/cc/oxshan/
├── controller/               # 控制器层
├── service/                  # 服务层
│   └── impl/                 # 服务实现
├── mapper/                   # MyBatis Mapper
├── model/                    # 实体类
├── dto/                      # 数据传输对象
├── bo/                       # 业务对象
├── config/                   # 配置类
├── common/                   # 公共类 (Result, Constants)
├── exception/                # 异常处理
└── utils/                    # 工具类

src/main/resources/
├── mapper/                   # MyBatis XML
├── application.properties    # 公共配置
├── application-dev.properties  # 开发环境配置
└── application-prod.properties # 生产环境配置
```

---

## 三、命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 类名 | PascalCase | `UserController`, `UserService` |
| 方法/变量 | camelCase | `getUserById()`, `userName` |
| 常量 | UPPER_SNAKE_CASE | `MAX_PAGE_SIZE` |
| 包名 | 全小写 | `cc.oxshan.service` |
| 数据库表名 | snake_case | `user_info` |
| 数据库字段 | snake_case | `created_at`, `user_name` |

---

## 四、分层规范

| 层级 | 职责 | 命名规范 |
|------|------|----------|
| Controller | 参数校验、调用 Service | `XxxController` |
| Service | 业务逻辑 | `XxxService` / `XxxServiceImpl` |
| Mapper | 数据访问 | `XxxMapper` |
| Model | 数据库实体 | `Xxx` |
| BO | 业务对象 | `XxxBO` |
| DTO | 请求参数对象 | `XxxReqDTO` |
| DTO | 响应对象 | `XxxRspDTO` |

---

## 五、API 设计

| 操作 | HTTP 方法 | 路径示例 |
|------|-----------|----------|
| 查询列表 | GET | `/api/users/list` |
| 查询详情 | GET | `/api/users/{id}` |
| 新增 | POST | `/api/users/add` |
| 修改 | POST | `/api/users/update` |
| 删除 | DELETE | `/api/users/{id}` |

---

## 六、统一响应格式

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

---

## 七、分页结果格式

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

---

## 八、异常处理

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

---

## 九、MyBatis 规范

- 必须使用 XML 文件完成CRUD，不允许使用注解
- XML 文件统一放在 `resources/mapper/` 目录
- 每个 Mapper XML 必须定义 `BaseResultMap` 和 `Base_Column_List`

### Mapper XML 示例

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

---

## 十、MySQL 表设计规范

### 基础约束

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
| is_deleted | TINYINT(1) | 否 | 逻辑删除标识，0-未删除，1-已删除 |

### 建表示例

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

## 十一、开发环境启动

```bash
mvn spring-boot:run
```

---

## 十二、多环境配置 (必须拆分 dev/prod)

### application.properties (公共配置)

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

### application-dev.properties (开发环境)

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

### application-prod.properties (生产环境)

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

---

## 十三、Docker 部署

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

---

## 十四、Git 提交规范

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

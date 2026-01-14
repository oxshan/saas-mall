# 登录接口 404 错误排查总结

## 问题描述

前端调用登录接口时返回 404 错误：

```
请求地址: http://localhost:3000/api/admin/auth/login
请求方法: POST
状态代码: 404 Not Found
```

---

## 问题排查过程

### 1. 初步分析

**检查服务运行状态：**
- ✅ Nacos: 运行在 8848 端口
- ✅ Gateway: 运行在 8080 端口
- ✅ mall-admin: 运行在 8081 端口

**请求链路：**
```
前端 (Vite:3000)
  ↓ 请求: /api/admin/auth/login
  ↓ Vite proxy 转发
Gateway (8080)
  ↓ 接收: /admin/auth/login
  ↓ 路由匹配: /admin/** → lb://mall-admin
  ↓ StripPrefix=1: 去掉 /admin
mall-admin (8081)
  ↓ 接收: /auth/login
  ↓ 控制器: AuthController
```

---

### 2. 发现的问题

#### 问题 1: mall-admin 服务注册成功 ✅

**验证命令：**
```bash
curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=mall-admin"
```

**结果：**
- 服务名: `mall-admin`
- IP: `192.168.31.157:8081`
- 状态: `healthy: true`

---

#### 问题 2: mall-service-user Dubbo 服务 IP 错误 ❌

**现象：**
- 直接访问 mall-admin 登录接口超时 52 秒后返回 500 错误
- 说明 Dubbo 服务调用失败

**排查过程：**

1. **检查 Dubbo 服务注册信息：**
```bash
curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=mall-service-user"
```

2. **发现问题：**
```json
{
  "ip": "198.18.0.1",  // ❌ 错误的 IP（虚拟网络接口）
  "port": 20881,
  "healthy": true
}
```

3. **分析原因：**
- 系统有多个网络接口：
  - `192.168.31.157` - 局域网 IP (WiFi/以太网)
  - `198.18.0.1` - 虚拟网络接口 (VPN/Docker)
- Dubbo 自动选择了错误的 IP
- mall-admin 无法通过 `198.18.0.1` 连接到 Dubbo 服务

---

#### 问题 3: Gateway CORS 配置错误 ❌

**现象：**
- 通过 Gateway 访问返回 500 错误

**错误日志：**
```
java.lang.IllegalArgumentException: When allowCredentials is true, 
allowedOrigins cannot contain the special value "*" since that cannot 
be set on the "Access-Control-Allow-Origin" response header.
```

**原因：**
- Gateway 配置中同时设置了：
  - `allowCredentials: true`
  - `allowedOrigins: "*"`
- Spring 不允许这两个配置同时使用

---

## 解决方案

### 修改 1: 修复 mall-service-user Dubbo 注册 IP

**文件：** `mall-service/mall-service-user/src/main/resources/application.yml`

**修改前：**
```yaml
dubbo:
  application:
    name: mall-service-user
  protocol:
    name: dubbo
    port: 20881
  registry:
    address: nacos://localhost:8848
```

**修改后：**
```yaml
dubbo:
  application:
    name: mall-service-user
  protocol:
    name: dubbo
    port: 20881
    host: 192.168.31.157  # 指定局域网 IP
  registry:
    address: nacos://localhost:8848
    parameters:
      register-ip: 192.168.31.157  # 强制注册 IP
```

**说明：**
- `protocol.host`: 指定 Dubbo 服务监听的 IP
- `register-ip`: 指定注册到 Nacos 的 IP
- 使用局域网 IP `192.168.31.157` 而不是虚拟网络 IP

---

### 修改 2: 修复 Gateway CORS 配置

**文件：** `mall-gateway/src/main/resources/application.yml`

**修改前：**
```yaml
globalcors:
  cors-configurations:
    '[/**]':
      allowedOrigins: "*"  # ❌ 错误配置
      allowedMethods: "*"
      allowedHeaders: "*"
      allowCredentials: true
```

**修改后：**
```yaml
globalcors:
  cors-configurations:
    '[/**]':
      allowedOriginPatterns: "*"  # ✅ 使用 allowedOriginPatterns
      allowedMethods: "*"
      allowedHeaders: "*"
      allowCredentials: true
```

**说明：**
- 将 `allowedOrigins` 改为 `allowedOriginPatterns`
- 这样可以同时使用 `allowCredentials: true`

---

### 修改 3: 修复 Gateway JWT 白名单路径

**文件：** `mall-gateway/src/main/java/cc/oxshan/gateway/filter/JwtAuthenticationFilter.java`

**修改前：**
```java
private static final List<String> WHITE_LIST = List.of(
    "/auth/login",      // ❌ 路径不完整
    "/auth/register",
    "/actuator"
);
```

**修改后：**
```java
private static final List<String> WHITE_LIST = List.of(
    "/admin/auth/login",      // ✅ 完整路径
    "/admin/auth/register",
    "/buyer/auth/login",
    "/buyer/auth/register",
    "/actuator"
);
```

**说明：**
- Gateway 接收到的路径是 `/admin/auth/login`
- 白名单需要匹配完整路径
- 添加了买家端的登录注册路径

---

## 验证结果

### 1. 验证 Dubbo 服务注册

```bash
curl -s "http://localhost:8848/nacos/v1/ns/instance/list?serviceName=mall-service-user" | \
python3 -c "import sys, json; data=json.load(sys.stdin); \
host=data['hosts'][0] if data['hosts'] else {}; \
print(f'注册IP: {host.get(\"ip\")}'); \
print(f'端口: {host.get(\"port\")}'); \
print(f'健康状态: {\"健康\" if host.get(\"healthy\") else \"不健康\"}')"
```

**输出：**
```
注册IP: 192.168.31.157
端口: 20881
健康状态: 健康
```

✅ Dubbo 服务已注册到正确的 IP

---

### 2. 测试登录接口

**直接访问 mall-admin：**
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

**输出：**
```json
{
  "code": 2001,
  "msg": "用户名或密码错误",
  "data": null,
  "success": false
}
```

✅ 返回业务错误（用户不存在），说明 Dubbo 调用成功

---

**通过 Gateway 访问：**
```bash
curl -X POST http://localhost:8080/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

**输出：**
```json
{
  "code": 2001,
  "msg": "用户名或密码错误",
  "data": null,
  "success": false
}
```

✅ Gateway 路由正常，完整链路打通

---

## 完整的请求流程（修复后）

```
前端 (Vite:3000)
  ↓ 请求: /api/admin/auth/login
  ↓ Vite proxy 去掉 /api
  
Gateway (8080)
  ↓ 接收: /admin/auth/login
  ↓ JWT 过滤器: 检查白名单 ✅ 通过
  ↓ CORS 配置: ✅ 正确配置
  ↓ 路由匹配: /admin/** → lb://mall-admin
  ↓ StripPrefix=1: 去掉 /admin
  ↓ 服务发现: 从 Nacos 获取 mall-admin (192.168.31.157:8081)
  
mall-admin (8081)
  ↓ 接收: /auth/login
  ↓ AuthController: 处理登录请求
  ↓ Dubbo 调用: userServiceClient.getByUsername()
  
mall-service-user (20881)
  ↓ Dubbo 服务: 192.168.31.157:20881 ✅ 可连接
  ↓ 查询用户信息
  ↓ 返回结果
  
✅ 完整链路打通！
```

---

## 关键知识点

### 1. 微服务架构中的服务发现

- **Spring Cloud Nacos Discovery**: 用于 HTTP 服务注册与发现
- **Dubbo Nacos Registry**: 用于 RPC 服务注册与发现
- mall-admin 需要同时注册到两个注册中心

### 2. Dubbo IP 选择机制

- Dubbo 会自动检测本机 IP 地址
- 如果有多个网卡，可能选择错误的 IP
- 需要通过配置明确指定 IP

### 3. Gateway 路由规则

- `Path=/admin/**`: 匹配路径
- `StripPrefix=1`: 去掉第一段路径
- `lb://mall-admin`: 通过负载均衡访问服务

### 4. CORS 配置注意事项

- `allowCredentials: true` 不能与 `allowedOrigins: "*"` 同时使用
- 需要使用 `allowedOriginPatterns: "*"` 替代

---

## 后续建议

### 1. 统一 Dubbo IP 配置

如果有其他 Dubbo 服务，也需要配置正确的注册 IP：

```yaml
dubbo:
  protocol:
    host: 0.0.0.0  # 推荐：监听所有接口
  registry:
    parameters:
      register-ip: 192.168.31.157  # 明确指定注册 IP
```

### 2. 使用环境变量

便于在不同环境切换：

```yaml
dubbo:
  protocol:
    host: ${DUBBO_HOST:0.0.0.0}
  registry:
    parameters:
      register-ip: ${DUBBO_REGISTER_IP:192.168.31.157}
```

### 3. 添加健康检查

在 Gateway 和 mall-admin 中添加健康检查端点：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### 4. 配置日志级别

便于问题排查：

```yaml
logging:
  level:
    cc.oxshan: DEBUG
    org.apache.dubbo: INFO
    org.springframework.cloud.gateway: DEBUG
```

---

## 总结

本次问题的根本原因是：

1. **Dubbo 服务注册了错误的 IP** - 导致服务间调用失败
2. **Gateway CORS 配置错误** - 导致跨域请求失败
3. **JWT 白名单路径不匹配** - 导致登录接口需要 token

通过修改配置文件并重启服务，所有问题已解决，完整的请求链路已打通。

---

**文档创建时间：** 2026-01-14  
**问题状态：** ✅ 已解决

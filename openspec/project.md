# Project Context

## Purpose
SaaS 连锁商城系统 - 一个面向连锁零售企业的多租户电商平台，支持总部-门店的组织架构，提供商品管理、订单处理、会员营销、库存管理等完整的零售业务能力。

## Tech Stack

### 后端
- Java 17 + Spring Boot 3.2.0
- Spring Cloud 2023.0.0 + Spring Cloud Alibaba 2023.0.0.0-RC1
- Apache Dubbo 3.3.0 (RPC 通信)
- MyBatis Plus 3.5.5 (ORM)
- Sa-Token 1.37.0 (认证授权)
- MySQL 8.0, Redis 7, Nacos 2.3.0, RocketMQ 5.1.4, Elasticsearch 8.11.0

### 前端 (规划中)
- React 18 + TypeScript + Vite
- Tailwind CSS + Ant Design
- Zustand (状态管理)

## Project Conventions

### Code Style
- 后端遵循 `CONVENTIONS.md` 中的 Java 命名规范
- 类名 PascalCase，方法/变量 camelCase，常量 UPPER_SNAKE_CASE
- 数据库表名和字段使用 snake_case
- MyBatis 必须使用 XML 方式，禁止注解

### Architecture Patterns
- **微服务架构**: 16 个独立服务通过 Dubbo RPC 通信
- **BFF 模式**: mall-admin (商家后台) / mall-buyer (买家端)
- **接口与实现分离**: mall-api 定义接口，mall-service 实现
- **多租户隔离**: 通过 shop_id 实现租户数据隔离
- **公共模块**: mall-common 提供 core/redis/mybatis/rocketmq/security 等基础组件

### Testing Strategy
- 单元测试: JUnit 5
- 集成测试: Spring Boot Test
- 跳过测试构建: `mvn package -DskipTests`

### Git Workflow
- 提交格式: `<type>: <description>`
- Type: feat/fix/docs/style/refactor/test/chore

## Domain Context
- **租户 (Tenant)**: SaaS 平台的客户企业
- **店铺 (Shop)**: 租户下的门店，分为总部店和分店
- **会员 (Member)**: C 端消费者
- **用户 (User)**: B 端商家后台用户
- **ShopContext**: 多租户上下文，通过 ThreadLocal 传递 shop_id

## Important Constraints
- 所有业务数据必须通过 shop_id 隔离
- API 响应统一使用 `R<T>` 封装
- 分页响应使用 `PageResult<T>` 封装
- 业务异常使用 `BizException`

## External Dependencies
- **Nacos**: 服务注册与配置中心
- **RocketMQ**: 异步消息队列
- **Elasticsearch**: 商品搜索引擎
- **Redis**: 缓存与分布式锁 (Redisson)
- **支付网关**: 待集成

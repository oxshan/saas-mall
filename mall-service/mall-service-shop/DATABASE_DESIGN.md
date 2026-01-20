# 数据库设计文档

## 一、概述

### 1.1 设计原则

- 数据隔离：不同店铺数据通过 parent_id 实现隔离
- 逻辑删除：使用 is_deleted 字段标记删除
- 时间戳：所有表包含 created_at 和 updated_at
- 编码唯一：店铺编码全局唯一

### 1.2 表清单

| 表名 | 说明 | 备注 |
|------|------|------|
| shop | 店铺表 | 单店/总部/分店 |
| user_shop | 用户店铺关联表 | 多对多关系 |
| code_sequence | 编码序列表 | 生成唯一编码 |

---

## 二、表结构设计

### 2.1 店铺表（shop）

**表说明：** 存储店铺基本信息，支持单店、连锁总部、连锁分店

**建表语句：**

```sql
CREATE TABLE IF NOT EXISTS `shop` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父店铺ID（0=总部或单店）',
    `shop_code` VARCHAR(64) NOT NULL COMMENT '店铺编码（系统生成，格式：SHOP20250120001）',
    `shop_name` VARCHAR(128) NOT NULL COMMENT '店铺名称',
    `shop_type` TINYINT NOT NULL DEFAULT 1 COMMENT '店铺类型：1-单店，2-连锁总部，3-连锁分店',
    `province` VARCHAR(64) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(64) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(64) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `longitude` DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
    `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `business_hours` VARCHAR(64) DEFAULT NULL COMMENT '营业时间（格式：09:00-21:00）',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT '店铺Logo',
    `images` TEXT DEFAULT NULL COMMENT '店铺图片（JSON数组）',
    `description` TEXT DEFAULT NULL COMMENT '店铺描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停业，1-营业',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_shop_code` (`shop_code`),
    KEY `idx_tenant_id` (`tenant_id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_shop_type` (`shop_type`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';
```

**字段说明：**

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| id | BIGINT | 主键ID | 自增 |
| parent_id | BIGINT | 父店铺ID | 0=总部或单店 |
| shop_code | VARCHAR(64) | 店铺编码 | 系统生成，全局唯一 |
| shop_name | VARCHAR(128) | 店铺名称 | 必填 |
| shop_type | TINYINT | 店铺类型 | 1-单店，2-连锁总部，3-连锁分店 |
| province | VARCHAR(64) | 省份 | 可选 |
| city | VARCHAR(64) | 城市 | 可选 |
| district | VARCHAR(64) | 区县 | 可选 |
| address | VARCHAR(255) | 详细地址 | 可选 |
| longitude | DECIMAL(10,6) | 经度 | 地图定位 |
| latitude | DECIMAL(10,6) | 纬度 | 地图定位 |
| contact_name | VARCHAR(64) | 联系人 | 可选 |
| contact_phone | VARCHAR(20) | 联系电话 | 可选 |
| business_hours | VARCHAR(64) | 营业时间 | 格式：09:00-21:00 |
| logo | VARCHAR(255) | 店铺Logo | URL地址 |
| images | TEXT | 店铺图片 | JSON数组 |
| description | TEXT | 店铺描述 | 富文本 |
| status | TINYINT | 状态 | 0-停业，1-营业 |
| is_deleted | TINYINT | 是否删除 | 0-否，1-是 |
| sort | INT | 排序 | 数字越小越靠前 |
| created_by | BIGINT | 创建人ID | 关联 sys_user.id |
| created_at | DATETIME | 创建时间 | 自动填充 |
| updated_at | DATETIME | 更新时间 | 自动更新 |

**索引说明：**

- `uk_shop_code`：店铺编码唯一索引
- `idx_parent_id`：父店铺ID索引（查询总部的所有分店）
- `idx_shop_type`：店铺类型索引（按类型查询）
- `idx_status`：状态索引（查询营业/停业店铺）
- `idx_is_deleted`：删除标记索引（过滤已删除数据）

---

### 2.2 用户店铺关联表（user_shop）

**表说明：** 用户与店铺的多对多关联关系，支持一个用户管理多个店铺

**建表语句：**

```sql
CREATE TABLE IF NOT EXISTS `user_shop` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认店铺：0-否，1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`user_id`, `shop_id`),
    KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户店铺关联表';
```

**字段说明：**

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| user_id | BIGINT | 用户ID | 关联 sys_user.id |
| shop_id | BIGINT | 店铺ID | 关联 shop.id |
| is_default | TINYINT | 是否默认店铺 | 0-否，1-是 |
| created_at | DATETIME | 创建时间 | 自动填充 |

**索引说明：**

- `PRIMARY KEY (user_id, shop_id)`：联合主键
- `idx_shop_id`：店铺ID索引（查询店铺的所有管理员）

---

### 2.3 编码序列表（code_sequence）

**表说明：** 用于生成唯一编码（租户编码、店铺编码等）

**建表语句：**

```sql
CREATE TABLE IF NOT EXISTS `code_sequence` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `prefix` VARCHAR(32) NOT NULL COMMENT '编码前缀（SHOP等）',
    `date_part` VARCHAR(16) NOT NULL COMMENT '日期部分（YYYYMMDD）',
    `sequence` INT NOT NULL DEFAULT 0 COMMENT '当日序号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_prefix_date` (`prefix`, `date_part`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='编码序列表';
```

**字段说明：**

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| id | BIGINT | 主键ID | 自增 |
| prefix | VARCHAR(32) | 编码前缀 | SHOP等 |
| date_part | VARCHAR(16) | 日期部分 | 格式：YYYYMMDD |
| sequence | INT | 当日序号 | 从1开始递增 |
| created_at | DATETIME | 创建时间 | 自动填充 |
| updated_at | DATETIME | 更新时间 | 自动更新 |

**索引说明：**

- `uk_prefix_date`：前缀+日期唯一索引（保证每天的序号唯一）

**编码生成规则：**

- 店铺编码：`SHOP + YYYYMMDD + 序号（3位）`，例如：SHOP20250120001

---

## 三、业务逻辑说明

### 3.1 商户自助注册流程

**步骤1：用户注册**
1. 用户填写注册信息（用户名、密码、手机号等）
2. 系统创建用户账号（sys_user 表）

**步骤2：创建店铺**
1. 用户选择店铺类型（单店 or 连锁总部）
2. 填写店铺信息（店铺名称、地址等）
3. 系统执行：
   - 生成店铺编码（SHOP20250120001）
   - 创建店铺记录（单店：shop_type=1，连锁总部：shop_type=2）
   - 自动关联用户到店铺（user_shop 表，is_default=1）
   - 为用户分配默认角色（单店店长 or 总部管理员）

**步骤3：完成注册**
- 跳转到店铺管理页面

---

### 3.2 创建分店流程

**前置条件：**
- 用户已有连锁总部店铺
- 用户角色为总部管理员

**步骤：**
1. 进入店铺管理页面，点击"新增分店"
2. 填写分店信息（店铺名称、地址等）
3. 系统执行：
   - 生成店铺编码（SHOP20250120002）
   - 创建分店记录（shop_type=3, parent_id=总部ID）
   - 可选：分配用户到分店

---

### 3.3 用户切换店铺流程

**步骤：**
1. 用户点击顶部导航栏的店铺切换按钮
2. 展开下拉菜单，显示用户可管理的所有店铺
3. 选择目标店铺
4. 系统执行：
   - 更新当前会话的 currentShopId
   - 刷新页面数据（基于新的店铺ID）

---

## 四、数据权限控制

### 4.1 权限过滤规则

| 角色类型 | 权限范围 | 查询条件 |
|---------|---------|---------|
| 超级管理员 | 所有店铺 | 无限制 |
| 总部管理员 | 总部和所有分店 | parent_id = 0 或 parent_id = 当前店铺ID |
| 店长/店员（分店） | 当前店铺 | shop_id = 当前店铺ID |
| 店长/店员（单店） | 当前店铺 | shop_id = 当前店铺ID |

### 4.2 数据隔离规则

**店铺级权限：**
- 总部用户：可以查看所有分店数据（parent_id=0 或 id=parent_id）
- 分店用户：只能查看自己店铺数据（shop_id=当前店铺ID）
- 单店用户：只能查看自己店铺数据

---

## 五、完整建表脚本

```sql
-- 店铺表
CREATE TABLE IF NOT EXISTS `shop` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父店铺ID（0=总部或单店）',
    `shop_code` VARCHAR(64) NOT NULL COMMENT '店铺编码',
    `shop_name` VARCHAR(128) NOT NULL COMMENT '店铺名称',
    `shop_type` TINYINT NOT NULL DEFAULT 1 COMMENT '店铺类型：1-单店，2-连锁总部，3-连锁分店',
    `province` VARCHAR(64) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(64) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(64) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `longitude` DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
    `latitude` DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
    `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `business_hours` VARCHAR(64) DEFAULT NULL COMMENT '营业时间',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT '店铺Logo',
    `images` TEXT DEFAULT NULL COMMENT '店铺图片',
    `description` TEXT DEFAULT NULL COMMENT '店铺描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停业，1-营业',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_shop_code` (`shop_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_shop_type` (`shop_type`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';

-- 用户店铺关联表
CREATE TABLE IF NOT EXISTS `user_shop` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `shop_id` BIGINT NOT NULL COMMENT '店铺ID',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认店铺：0-否，1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`user_id`, `shop_id`),
    KEY `idx_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户店铺关联表';

-- 编码序列表
CREATE TABLE IF NOT EXISTS `code_sequence` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `prefix` VARCHAR(32) NOT NULL COMMENT '编码前缀（SHOP等）',
    `date_part` VARCHAR(16) NOT NULL COMMENT '日期部分（YYYYMMDD）',
    `sequence` INT NOT NULL DEFAULT 0 COMMENT '当日序号',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_prefix_date` (`prefix`, `date_part`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='编码序列表';
```

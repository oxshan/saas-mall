-- 店铺表
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

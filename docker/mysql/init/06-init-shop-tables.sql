-- =============================================
-- 店铺模块表结构
-- =============================================

USE saas_mall;

-- ----------------------------
-- 1. 租户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tenant` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '租户ID',
    `tenant_code` VARCHAR(64) NOT NULL COMMENT '租户编码（系统生成，格式：TENANT20250120001）',
    `tenant_name` VARCHAR(128) NOT NULL COMMENT '租户名称',
    `tenant_type` TINYINT NOT NULL DEFAULT 1 COMMENT '租户类型：1-单店，2-连锁品牌',
    `contact_name` VARCHAR(64) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(128) DEFAULT NULL COMMENT '联系邮箱',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT '品牌Logo',
    `description` TEXT DEFAULT NULL COMMENT '品牌描述',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `expired_at` DATETIME DEFAULT NULL COMMENT '到期时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_code` (`tenant_code`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户表';

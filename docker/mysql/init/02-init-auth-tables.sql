-- =============================================
-- 权限系统表结构
-- =============================================

USE saas_mall;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `shop_id` BIGINT NOT NULL DEFAULT 0 COMMENT '所属店铺ID（0=平台级）',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `nickname` VARCHAR(64) DEFAULT NULL COMMENT '昵称',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ----------------------------
-- 2. 角色表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `shop_id` BIGINT NOT NULL DEFAULT 0 COMMENT '所属店铺ID（0=系统模板）',
    `template_id` BIGINT DEFAULT NULL COMMENT '来源模板角色ID',
    `code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `name` VARCHAR(64) NOT NULL COMMENT '角色名称',
    `type` TINYINT NOT NULL DEFAULT 2 COMMENT '角色类型：0-超级管理员，1-总部，2-门店',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统内置：0-否，1-是（不可删除）',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- ----------------------------
-- 3. 菜单权限表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID（0=顶级）',
    `name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
    `path` VARCHAR(255) DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    `perms` VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型：0-目录，1-菜单，2-按钮',
    `icon` VARCHAR(64) DEFAULT NULL COMMENT '图标',
    `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见：0-隐藏，1-显示',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

-- ----------------------------
-- 4. 用户角色关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ----------------------------
-- 5. 角色菜单关联表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- =============================================
-- 初始化数据
-- =============================================

-- ----------------------------
-- 初始化角色模板
-- ----------------------------
INSERT INTO `sys_role` (`id`, `shop_id`, `code`, `name`, `type`, `is_system`, `sort`, `remark`) VALUES
(1, 0, 'super_admin', '超级管理员', 0, 1, 0, '系统内置超级管理员，拥有所有权限'),
(2, 0, 'hq_admin', '总部管理员', 1, 1, 1, '总部管理员模板'),
(3, 0, 'shop_manager', '店长', 2, 1, 2, '门店店长模板'),
(4, 0, 'shop_staff', '店员', 2, 1, 3, '门店店员模板');

-- ----------------------------
-- 初始化超级管理员用户
-- 密码: admin123 (BCrypt加密)
-- ----------------------------
INSERT INTO `sys_user` (`id`, `shop_id`, `username`, `password`, `nickname`, `status`) VALUES
(1, 0, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 1);

-- ----------------------------
-- 初始化用户角色关联
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- ----------------------------
-- 初始化系统菜单
-- ----------------------------
-- 系统管理目录
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `perms`, `type`, `icon`, `sort`) VALUES
(1, 0, '系统管理', '/system', NULL, NULL, 0, 'setting', 1);

-- 用户管理菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `perms`, `type`, `icon`, `sort`) VALUES
(2, 1, '用户管理', '/system/user', 'system/user/index', 'system:user:list', 1, 'user', 1);

-- 用户管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `perms`, `type`, `sort`) VALUES
(3, 2, '新增用户', 'system:user:add', 2, 1),
(4, 2, '编辑用户', 'system:user:edit', 2, 2),
(5, 2, '删除用户', 'system:user:delete', 2, 3);

-- 角色管理菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `perms`, `type`, `icon`, `sort`) VALUES
(6, 1, '角色管理', '/system/role', 'system/role/index', 'system:role:list', 1, 'peoples', 2);

-- 角色管理按钮
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `perms`, `type`, `sort`) VALUES
(7, 6, '新增角色', 'system:role:add', 2, 1),
(8, 6, '编辑角色', 'system:role:edit', 2, 2),
(9, 6, '删除角色', 'system:role:delete', 2, 3);

-- 菜单管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `perms`, `type`, `icon`, `sort`) VALUES
(10, 1, '菜单管理', '/system/menu', 'system/menu/index', 'system:menu:list', 1, 'tree-table', 3);

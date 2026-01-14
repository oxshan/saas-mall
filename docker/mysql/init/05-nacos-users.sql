-- Nacos 用户权限表
USE nacos_config;

CREATE TABLE IF NOT EXISTS users (
  username varchar(50) NOT NULL PRIMARY KEY,
  password varchar(500) NOT NULL,
  enabled boolean NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
  username varchar(50) NOT NULL,
  role varchar(50) NOT NULL,
  UNIQUE KEY idx_user_role (username, role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS permissions (
  role varchar(50) NOT NULL,
  resource varchar(255) NOT NULL,
  action varchar(8) NOT NULL,
  UNIQUE KEY uk_role_permission (role,resource,action)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 默认用户 nacos/nacos
INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);
INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');

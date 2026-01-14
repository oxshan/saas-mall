-- 开发环境：使用单库 test
-- 生产环境请参考 01-init-databases.sql.prod

CREATE DATABASE IF NOT EXISTS saas_mall DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Nacos 配置库
CREATE DATABASE IF NOT EXISTS nacos_config DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Nacos 配置表 (续)
USE nacos_config;

CREATE TABLE IF NOT EXISTS tenant_info (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  kp varchar(128) NOT NULL,
  tenant_id varchar(128) DEFAULT '',
  tenant_name varchar(128) DEFAULT '',
  tenant_desc varchar(256) DEFAULT NULL,
  create_source varchar(32) DEFAULT NULL,
  gmt_create bigint(20) NOT NULL,
  gmt_modified bigint(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_tenant_info_kptenantid (kp,tenant_id),
  KEY idx_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS tenant_capacity (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  tenant_id varchar(128) NOT NULL DEFAULT '',
  quota int(10) unsigned NOT NULL DEFAULT '0',
  usage int(10) unsigned NOT NULL DEFAULT '0',
  max_size int(10) unsigned NOT NULL DEFAULT '0',
  max_aggr_count int(10) unsigned NOT NULL DEFAULT '0',
  max_aggr_size int(10) unsigned NOT NULL DEFAULT '0',
  max_history_count int(10) unsigned NOT NULL DEFAULT '0',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_tenant_id (tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS group_capacity (
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  group_id varchar(128) NOT NULL DEFAULT '',
  quota int(10) unsigned NOT NULL DEFAULT '0',
  usage int(10) unsigned NOT NULL DEFAULT '0',
  max_size int(10) unsigned NOT NULL DEFAULT '0',
  max_aggr_count int(10) unsigned NOT NULL DEFAULT '0',
  max_aggr_size int(10) unsigned NOT NULL DEFAULT '0',
  max_history_count int(10) unsigned NOT NULL DEFAULT '0',
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_group_id (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

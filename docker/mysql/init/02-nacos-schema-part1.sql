-- Nacos 配置表
USE nacos_config;

CREATE TABLE IF NOT EXISTS config_info (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) DEFAULT NULL,
  content longtext NOT NULL,
  md5 varchar(32) DEFAULT NULL,
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  src_user text,
  src_ip varchar(50) DEFAULT NULL,
  app_name varchar(128) DEFAULT NULL,
  tenant_id varchar(128) DEFAULT '',
  c_desc varchar(256) DEFAULT NULL,
  c_use varchar(64) DEFAULT NULL,
  effect varchar(64) DEFAULT NULL,
  type varchar(64) DEFAULT NULL,
  c_schema text,
  encrypted_data_key text,
  PRIMARY KEY (id),
  UNIQUE KEY uk_configinfo_datagrouptenant (data_id,group_id,tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS config_info_beta (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  app_name varchar(128) DEFAULT NULL,
  content longtext NOT NULL,
  beta_ips varchar(1024) DEFAULT NULL,
  md5 varchar(32) DEFAULT NULL,
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  src_user text,
  src_ip varchar(50) DEFAULT NULL,
  tenant_id varchar(128) DEFAULT '',
  encrypted_data_key text,
  PRIMARY KEY (id),
  UNIQUE KEY uk_configinfobeta_datagrouptenant (data_id,group_id,tenant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS config_info_tag (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) DEFAULT '',
  tag_id varchar(128) NOT NULL,
  app_name varchar(128) DEFAULT NULL,
  content longtext NOT NULL,
  md5 varchar(32) DEFAULT NULL,
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  src_user text,
  src_ip varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_configinfotag_datagrouptenanttag (data_id,group_id,tenant_id,tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

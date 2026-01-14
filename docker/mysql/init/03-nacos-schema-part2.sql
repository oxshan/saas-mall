-- Nacos 配置表 (续)
USE nacos_config;

CREATE TABLE IF NOT EXISTS config_info_aggr (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  datum_id varchar(255) NOT NULL,
  content longtext NOT NULL,
  gmt_modified datetime NOT NULL,
  app_name varchar(128) DEFAULT NULL,
  tenant_id varchar(128) DEFAULT '',
  PRIMARY KEY (id),
  UNIQUE KEY uk_configinfoaggr_datagrouptenantdatum (data_id,group_id,tenant_id,datum_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS his_config_info (
  id bigint(20) unsigned NOT NULL,
  nid bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  app_name varchar(128) DEFAULT NULL,
  content longtext NOT NULL,
  md5 varchar(32) DEFAULT NULL,
  gmt_create datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  gmt_modified datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  src_user text,
  src_ip varchar(50) DEFAULT NULL,
  op_type char(10) DEFAULT NULL,
  tenant_id varchar(128) DEFAULT '',
  encrypted_data_key text,
  PRIMARY KEY (nid),
  KEY idx_gmt_create (gmt_create),
  KEY idx_gmt_modified (gmt_modified),
  KEY idx_did (data_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

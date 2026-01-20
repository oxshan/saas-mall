package cc.oxshan.common.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 七牛云配置属性
 */
@Data
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties {
    
    /**
     * AccessKey
     */
    private String accessKey;
    
    /**
     * SecretKey
     */
    private String secretKey;
    
    /**
     * 存储空间名称
     */
    private String bucket;
    
    /**
     * CDN 域名
     */
    private String domain;
    
    /**
     * 上传区域（华东、华北、华南等）
     */
    private String region = "z0";
}

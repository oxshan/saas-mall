package cc.oxshan.common.upload.config;

import cc.oxshan.common.upload.service.QiniuUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云上传自动配置类
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
@ConditionalOnProperty(prefix = "qiniu", name = "access-key")
public class QiniuAutoConfiguration {

    public QiniuAutoConfiguration() {
        log.info("七牛云上传模块自动配置已启用");
    }

    @Bean
    @ConditionalOnMissingBean
    public QiniuUploadService qiniuUploadService(QiniuProperties qiniuProperties) {
        log.info("初始化 QiniuUploadService，bucket: {}, domain: {}", 
                 qiniuProperties.getBucket(), qiniuProperties.getDomain());
        return new QiniuUploadService(qiniuProperties);
    }
}

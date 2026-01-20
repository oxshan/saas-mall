package cc.oxshan.common.upload.service;

import cc.oxshan.common.upload.config.QiniuProperties;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;

/**
 * 七牛云上传服务
 */
@Slf4j
public class QiniuUploadService {

    private final QiniuProperties qiniuProperties;

    public QiniuUploadService(QiniuProperties qiniuProperties) {
        this.qiniuProperties = qiniuProperties;
    }

    /**
     * 获取上传 Token
     *
     * @return 上传 Token
     */
    public String getUploadToken() {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        return auth.uploadToken(qiniuProperties.getBucket());
    }

    /**
     * 获取上传 Token（带过期时间）
     *
     * @param expireSeconds 过期时间（秒）
     * @return 上传 Token
     */
    public String getUploadToken(long expireSeconds) {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        return auth.uploadToken(qiniuProperties.getBucket(), null, expireSeconds, null);
    }

    /**
     * 获取 CDN 域名
     *
     * @return CDN 域名
     */
    public String getDomain() {
        return qiniuProperties.getDomain();
    }

    /**
     * 生成文件访问 URL
     *
     * @param key 文件 key
     * @return 完整的访问 URL
     */
    public String getFileUrl(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        String domain = qiniuProperties.getDomain();
        if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
            domain = "https://" + domain;
        }
        if (!domain.endsWith("/")) {
            domain += "/";
        }
        return domain + key;
    }
}

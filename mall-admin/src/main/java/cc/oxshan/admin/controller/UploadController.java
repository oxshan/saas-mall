package cc.oxshan.admin.controller;

import cc.oxshan.common.upload.service.QiniuUploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private QiniuUploadService qiniuUploadService;

    /**
     * 获取七牛云上传 Token
     *
     * @return 上传 Token 和相关配置
     */
    @GetMapping("/token")
    public Map<String, Object> getUploadToken() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", qiniuUploadService.getUploadToken(3600)); // 1小时过期
        data.put("domain", qiniuUploadService.getDomain());
        
        result.put("data", data);
        return result;
    }
}

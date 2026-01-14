package cc.oxshan.admin.service;

import cc.oxshan.admin.client.SysUserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 权限校验服务
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final SysUserServiceClient userServiceClient;

    /**
     * 检查用户是否拥有指定权限
     */
    public boolean hasPermission(Long userId, String permission) {
        // 1. 检查是否超级管理员
        if (userServiceClient.isSuperAdmin(userId)) {
            return true;
        }
        
        // 2. 获取用户权限
        Set<String> permissions = userServiceClient.getPermissions(userId);
        
        // 3. 校验权限
        return permissions.contains(permission);
    }
}

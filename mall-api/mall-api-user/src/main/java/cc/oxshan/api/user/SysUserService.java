package cc.oxshan.api.user;

import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.result.rpc.PlainResult;

import java.util.Set;

/**
 * 用户服务接口 (Dubbo)
 */
public interface SysUserService {

    /**
     * 根据用户名查询用户
     */
    PlainResult<SysUserDTO> getByUsername(String username);

    /**
     * 根据用户ID查询权限标识
     */
    PlainResult<Set<String>> getPermissions(Long userId);

    /**
     * 判断用户是否为超级管理员
     */
    PlainResult<Boolean> isSuperAdmin(Long userId);
}

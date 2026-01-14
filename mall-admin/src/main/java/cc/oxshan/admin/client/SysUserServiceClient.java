package cc.oxshan.admin.client;

import cc.oxshan.api.user.SysUserService;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.rpc.PlainResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 用户服务 Client (拆包层)
 */
@Component
public class SysUserServiceClient {

    @DubboReference
    private SysUserService sysUserService;

    /**
     * 根据用户名查询用户
     */
    public SysUserDTO getByUsername(String username) {
        PlainResult<SysUserDTO> result = sysUserService.getByUsername(username);
        checkResult(result);
        return result.getData();
    }

    /**
     * 根据用户ID查询权限标识
     */
    public Set<String> getPermissions(Long userId) {
        PlainResult<Set<String>> result = sysUserService.getPermissions(userId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 判断用户是否为超级管理员
     */
    public boolean isSuperAdmin(Long userId) {
        PlainResult<Boolean> result = sysUserService.isSuperAdmin(userId);
        checkResult(result);
        return Boolean.TRUE.equals(result.getData());
    }

    private void checkResult(PlainResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}

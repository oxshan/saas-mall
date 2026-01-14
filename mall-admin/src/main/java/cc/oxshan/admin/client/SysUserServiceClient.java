package cc.oxshan.admin.client;

import cc.oxshan.api.user.SysUserService;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.exception.BizException;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
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

    /**
     * 分页查询用户列表
     */
    public PageResult<SysUserDTO> listUsers(Long shopId, Integer pageNum, Integer pageSize) {
        PlainResult<PageResult<SysUserDTO>> result = sysUserService.listUsers(shopId, pageNum, pageSize);
        checkResult(result);
        return result.getData();
    }

    /**
     * 根据ID查询用户
     */
    public SysUserDTO getUserById(Long userId) {
        PlainResult<SysUserDTO> result = sysUserService.getUserById(userId);
        checkResult(result);
        return result.getData();
    }

    /**
     * 创建用户
     */
    public Long createUser(SysUserDTO dto) {
        PlainResult<Long> result = sysUserService.createUser(dto);
        checkResult(result);
        return result.getData();
    }

    /**
     * 更新用户
     */
    public void updateUser(SysUserDTO dto) {
        PlainResult<Void> result = sysUserService.updateUser(dto);
        checkResult(result);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long userId) {
        PlainResult<Void> result = sysUserService.deleteUser(userId);
        checkResult(result);
    }

    /**
     * 重置密码
     */
    public void resetPassword(Long userId, String newPassword) {
        PlainResult<Void> result = sysUserService.resetPassword(userId, newPassword);
        checkResult(result);
    }

    /**
     * 分配角色
     */
    public void assignRoles(Long userId, List<Long> roleIds) {
        PlainResult<Void> result = sysUserService.assignRoles(userId, roleIds);
        checkResult(result);
    }

    private void checkResult(PlainResult<?> result) {
        if (result == null || !result.getSuccess()) {
            String msg = result != null ? result.getMsg() : "RPC调用失败";
            int code = result != null ? result.getCode() : 500;
            throw new BizException(code, msg);
        }
    }
}

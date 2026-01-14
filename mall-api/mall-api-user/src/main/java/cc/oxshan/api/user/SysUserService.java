package cc.oxshan.api.user;

import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.PlainResult;

import java.util.List;
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

    /**
     * 分页查询用户列表
     */
    PlainResult<PageResult<SysUserDTO>> listUsers(Long shopId, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询用户
     */
    PlainResult<SysUserDTO> getUserById(Long userId);

    /**
     * 创建用户
     */
    PlainResult<Long> createUser(SysUserDTO dto);

    /**
     * 更新用户
     */
    PlainResult<Void> updateUser(SysUserDTO dto);

    /**
     * 删除用户
     */
    PlainResult<Void> deleteUser(Long userId);

    /**
     * 重置密码
     */
    PlainResult<Void> resetPassword(Long userId, String newPassword);

    /**
     * 分配角色
     */
    PlainResult<Void> assignRoles(Long userId, List<Long> roleIds);
}

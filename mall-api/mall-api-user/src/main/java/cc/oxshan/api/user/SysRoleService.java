package cc.oxshan.api.user;

import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;

import java.util.List;

/**
 * 角色服务接口 (Dubbo)
 */
public interface SysRoleService {

    /**
     * 根据用户ID查询角色列表
     */
    ListResult<SysRoleDTO> getRolesByUserId(Long userId);

    /**
     * 分页查询角色列表
     */
    PlainResult<PageResult<SysRoleDTO>> listRoles(Long shopId, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询角色
     */
    PlainResult<SysRoleDTO> getRoleById(Long roleId);

    /**
     * 创建角色
     */
    PlainResult<Long> createRole(SysRoleDTO dto);

    /**
     * 更新角色
     */
    PlainResult<Void> updateRole(SysRoleDTO dto);

    /**
     * 删除角色
     */
    PlainResult<Void> deleteRole(Long roleId);

    /**
     * 分配菜单权限
     */
    PlainResult<Void> assignMenus(Long roleId, List<Long> menuIds);

    /**
     * 查询角色的菜单ID列表
     */
    ListResult<Long> getRoleMenuIds(Long roleId);
}

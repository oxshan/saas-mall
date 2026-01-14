package cc.oxshan.api.user;

import cc.oxshan.api.user.dto.SysMenuDTO;
import cc.oxshan.common.core.result.rpc.ListResult;
import cc.oxshan.common.core.result.rpc.PlainResult;

/**
 * 菜单服务接口 (Dubbo)
 */
public interface SysMenuService {

    /**
     * 查询菜单树（所有菜单）
     */
    ListResult<SysMenuDTO> getMenuTree();

    /**
     * 根据ID查询菜单
     */
    PlainResult<SysMenuDTO> getMenuById(Long menuId);

    /**
     * 创建菜单
     */
    PlainResult<Long> createMenu(SysMenuDTO dto);

    /**
     * 更新菜单
     */
    PlainResult<Void> updateMenu(SysMenuDTO dto);

    /**
     * 删除菜单
     */
    PlainResult<Void> deleteMenu(Long menuId);

    /**
     * 根据角色ID查询菜单
     */
    ListResult<SysMenuDTO> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单（用于前端菜单渲染）
     */
    ListResult<SysMenuDTO> getMenusByUserId(Long userId);
}

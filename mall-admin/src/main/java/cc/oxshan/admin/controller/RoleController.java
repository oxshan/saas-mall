package cc.oxshan.admin.controller;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.client.SysRoleServiceClient;
import cc.oxshan.admin.dto.AssignMenusDTO;
import cc.oxshan.api.user.dto.SysRoleDTO;
import cc.oxshan.common.core.context.ShopContext;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理 Controller
 */
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final SysRoleServiceClient roleServiceClient;

    /**
     * 角色列表（分页）
     */
    @RequiresPermission("system:role:list")
    @GetMapping("/list")
    public Result<PageResult<SysRoleDTO>> listRoles(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Long shopId = ShopContext.getShopId();
        PageResult<SysRoleDTO> page = roleServiceClient.listRoles(shopId, pageNum, pageSize);
        return Result.ok(page);
    }

    /**
     * 角色详情
     */
    @GetMapping("/{id}")
    public Result<SysRoleDTO> getRoleById(@PathVariable Long id) {
        SysRoleDTO role = roleServiceClient.getRoleById(id);
        return Result.ok(role);
    }

    /**
     * 新增角色
     */
    @RequiresPermission("system:role:add")
    @PostMapping("/add")
    public Result<Long> createRole(@RequestBody SysRoleDTO dto) {
        dto.setShopId(ShopContext.getShopId());
        Long roleId = roleServiceClient.createRole(dto);
        return Result.ok(roleId);
    }

    /**
     * 更新角色
     */
    @PostMapping("/update")
    public Result<Void> updateRole(@RequestBody SysRoleDTO dto) {
        roleServiceClient.updateRole(dto);
        return Result.ok();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleServiceClient.deleteRole(id);
        return Result.ok();
    }

    /**
     * 分配菜单权限
     */
    @PostMapping("/assign-menus")
    public Result<Void> assignMenus(@RequestBody AssignMenusDTO dto) {
        roleServiceClient.assignMenus(dto.getRoleId(), dto.getMenuIds());
        return Result.ok();
    }

    /**
     * 查询角色的菜单权限
     */
    @GetMapping("/{id}/menus")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long id) {
        List<Long> menuIds = roleServiceClient.getRoleMenuIds(id);
        return Result.ok(menuIds);
    }
}

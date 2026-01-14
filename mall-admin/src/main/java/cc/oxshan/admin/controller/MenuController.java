package cc.oxshan.admin.controller;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.client.SysMenuServiceClient;
import cc.oxshan.api.user.dto.SysMenuDTO;
import cc.oxshan.common.core.context.ShopContext;
import cc.oxshan.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理 Controller
 */
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final SysMenuServiceClient menuServiceClient;

    /**
     * 菜单树
     */
    @RequiresPermission("system:menu:list")
    @GetMapping("/tree")
    public Result<List<SysMenuDTO>> getMenuTree() {
        List<SysMenuDTO> tree = menuServiceClient.getMenuTree();
        return Result.ok(tree);
    }

    /**
     * 菜单详情
     */
    @GetMapping("/{id}")
    public Result<SysMenuDTO> getMenuById(@PathVariable Long id) {
        SysMenuDTO menu = menuServiceClient.getMenuById(id);
        return Result.ok(menu);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    public Result<Long> createMenu(@RequestBody SysMenuDTO dto) {
        Long menuId = menuServiceClient.createMenu(dto);
        return Result.ok(menuId);
    }

    /**
     * 更新菜单
     */
    @PostMapping("/update")
    public Result<Void> updateMenu(@RequestBody SysMenuDTO dto) {
        menuServiceClient.updateMenu(dto);
        return Result.ok();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenu(@PathVariable Long id) {
        menuServiceClient.deleteMenu(id);
        return Result.ok();
    }

    /**
     * 获取当前用户的菜单（用于前端渲染）
     */
    @GetMapping("/user-menus")
    public Result<List<SysMenuDTO>> getUserMenus() {
        Long userId = ShopContext.getUserId();
        List<SysMenuDTO> menus = menuServiceClient.getMenusByUserId(userId);
        return Result.ok(menus);
    }
}

package cc.oxshan.admin.controller;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.client.SysUserServiceClient;
import cc.oxshan.admin.dto.AssignRolesDTO;
import cc.oxshan.admin.dto.ResetPasswordDTO;
import cc.oxshan.admin.util.HeaderUtils;
import cc.oxshan.api.user.dto.SysUserDTO;
import cc.oxshan.common.core.context.ShopContext;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理 Controller
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserServiceClient userServiceClient;

    /**
     * 用户列表（分页）
     */
    @RequiresPermission("system:user:list")
    @GetMapping("/list")
    public Result<PageResult<SysUserDTO>> listUsers(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Long shopId = ShopContext.getShopId();
        PageResult<SysUserDTO> page = userServiceClient.listUsers(shopId, pageNum, pageSize);
        return Result.ok(page);
    }

    /**
     * 新增用户
     */
    @RequiresPermission("system:user:add")
    @PostMapping("/add")
    public Result<Long> createUser(@RequestBody SysUserDTO dto) {
        dto.setShopId(ShopContext.getShopId());
        Long userId = userServiceClient.createUser(dto);
        return Result.ok(userId);
    }

    /**
     * 更新用户
     */
    @RequiresPermission("system:user:edit")
    @PostMapping("/update")
    public Result<Void> updateUser(@RequestBody SysUserDTO dto) {
        userServiceClient.updateUser(dto);
        return Result.ok();
    }

    /**
     * 删除用户
     */
    @RequiresPermission("system:user:delete")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userServiceClient.deleteUser(id);
        return Result.ok();
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-pwd")
    public Result<Void> resetPassword(@RequestBody ResetPasswordDTO dto) {
        userServiceClient.resetPassword(dto.getUserId(), dto.getNewPassword());
        return Result.ok();
    }

    /**
     * 分配角色
     */
    @PostMapping("/assign-roles")
    public Result<Void> assignRoles(@RequestBody AssignRolesDTO dto) {
        userServiceClient.assignRoles(dto.getUserId(), dto.getRoleIds());
        return Result.ok();
    }
}

package cc.oxshan.admin.controller;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.client.UserShopServiceClient;
import cc.oxshan.admin.dto.AssignUserToShopDTO;
import cc.oxshan.admin.dto.RemoveUserFromShopDTO;
import cc.oxshan.admin.dto.SwitchShopDTO;
import cc.oxshan.admin.util.HeaderUtils;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户店铺关联 Controller
 */
@RestController
@RequestMapping("/user-shop")
@RequiredArgsConstructor
public class UserShopController {

    private final UserShopServiceClient userShopServiceClient;

    /**
     * 获取我的店铺列表
     */
    @GetMapping("/my-shops")
    public Result<List<ShopDTO>> getMyShops() {
        Long userId = HeaderUtils.getUserId();
        List<ShopDTO> shops = userShopServiceClient.getMyShops(userId);
        return Result.ok(shops);
    }

    /**
     * 切换店铺
     */
    @PostMapping("/switch")
    public Result<ShopDTO> switchShop(@RequestBody SwitchShopDTO dto) {
        Long userId = HeaderUtils.getUserId();
        ShopDTO shop = userShopServiceClient.switchShop(userId, dto.getShopId());
        return Result.ok(shop);
    }

    /**
     * 设置默认店铺
     */
    @PostMapping("/set-default")
    public Result<Void> setDefaultShop(@RequestBody SwitchShopDTO dto) {
        Long userId = HeaderUtils.getUserId();
        userShopServiceClient.setDefaultShop(userId, dto.getShopId());
        return Result.ok();
    }

    /**
     * 分配用户到店铺
     */
    @RequiresPermission("shop:user-shop:assign")
    @PostMapping("/assign")
    public Result<Void> assignUserToShop(@RequestBody AssignUserToShopDTO dto) {
        userShopServiceClient.assignUserToShop(dto.getUserId(), dto.getShopId(), dto.getIsDefault());
        return Result.ok();
    }

    /**
     * 移除用户店铺权限
     */
    @RequiresPermission("shop:user-shop:remove")
    @DeleteMapping("/remove")
    public Result<Void> removeUserFromShop(@RequestBody RemoveUserFromShopDTO dto) {
        userShopServiceClient.removeUserFromShop(dto.getUserId(), dto.getShopId());
        return Result.ok();
    }
}

package cc.oxshan.admin.controller;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.client.ShopServiceClient;
import cc.oxshan.admin.util.HeaderUtils;
import cc.oxshan.api.shop.dto.ShopCreateDTO;
import cc.oxshan.api.shop.dto.ShopDTO;
import cc.oxshan.api.shop.dto.ShopRegisterDTO;
import cc.oxshan.common.core.result.PageResult;
import cc.oxshan.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 店铺管理 Controller
 */
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopServiceClient shopServiceClient;

    /**
     * 店铺注册（创建单店或连锁总部）
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> registerShop(@RequestBody ShopRegisterDTO dto) {
        Long userId = HeaderUtils.getUserId();
        Map<String, Object> result = shopServiceClient.registerShop(dto, userId);
        return Result.ok(result);
    }

    /**
     * 创建店铺（创建分店）
     */
    @RequiresPermission("shop:shop:add")
    @PostMapping("/add")
    public Result<Map<String, Object>> createShop(@RequestBody ShopCreateDTO dto) {
        Long userId = HeaderUtils.getUserId();
        Long parentShopId = HeaderUtils.getShopId();
        Map<String, Object> result = shopServiceClient.createShop(dto, userId, parentShopId);
        return Result.ok(result);
    }

    /**
     * 店铺列表（分页）
     */
    @RequiresPermission("shop:shop:list")
    @GetMapping("/list")
    public Result<PageResult<ShopDTO>> listShops(
        @RequestParam(value = "parentId", required = false) Long parentId,
        @RequestParam(value = "shopName", required = false) String shopName,
        @RequestParam(value = "shopType", required = false) Integer shopType,
        @RequestParam(value = "status", required = false) Integer status,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        PageResult<ShopDTO> page = shopServiceClient.listShops(
            parentId, shopName, shopType, status, pageNum, pageSize);
        return Result.ok(page);
    }

    /**
     * 店铺详情
     */
    @RequiresPermission("shop:shop:detail")
    @GetMapping("/{id}")
    public Result<ShopDTO> getShopById(@PathVariable Long id) {
        ShopDTO shop = shopServiceClient.getShopById(id);
        return Result.ok(shop);
    }

    /**
     * 更新店铺
     */
    @RequiresPermission("shop:shop:edit")
    @PostMapping("/update")
    public Result<Void> updateShop(@RequestBody ShopDTO dto) {
        shopServiceClient.updateShop(dto);
        return Result.ok();
    }

    /**
     * 删除店铺
     */
    @RequiresPermission("shop:shop:delete")
    @DeleteMapping("/{id}")
    public Result<Void> deleteShop(@PathVariable Long id) {
        shopServiceClient.deleteShop(id);
        return Result.ok();
    }

    /**
     * 店铺树形结构
     */
    @RequiresPermission("shop:shop:tree")
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getShopTree() {
        List<Map<String, Object>> tree = shopServiceClient.getShopTree();
        return Result.ok(tree);
    }
}

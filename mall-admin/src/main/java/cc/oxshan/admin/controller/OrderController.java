package cc.oxshan.admin.controller;

import cc.oxshan.api.order.OrderService;
import cc.oxshan.api.order.dto.OrderDTO;
import cc.oxshan.common.core.context.ShopContext;
import cc.oxshan.common.core.result.Result;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理 Controller (商家后台)
 */
@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @DubboReference
    private OrderService orderService;

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public Result<List<OrderDTO>> list() {
        Long shopId = ShopContext.getShopId();
        List<OrderDTO> orders = orderService.listByShopId(shopId);
        return Result.ok(orders);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderDTO> detail(@PathVariable Long orderId) {
        Long shopId = ShopContext.getShopId();
        OrderDTO order = orderService.getById(shopId, orderId);
        return Result.ok(order);
    }
}

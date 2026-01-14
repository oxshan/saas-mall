package cc.oxshan.api.order;

import cc.oxshan.api.order.dto.OrderDTO;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 根据店铺ID查询订单列表
     */
    List<OrderDTO> listByShopId(Long shopId);

    /**
     * 根据订单ID查询
     */
    OrderDTO getById(Long shopId, Long orderId);
}

package cc.oxshan.order.service;

import cc.oxshan.api.order.OrderService;
import cc.oxshan.api.order.dto.OrderDTO;
import org.apache.dubbo.config.annotation.DubboService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现
 */
@DubboService
public class OrderServiceImpl implements OrderService {

    @Override
    public List<OrderDTO> listByShopId(Long shopId) {
        // TODO: 实际从数据库查询
        List<OrderDTO> list = new ArrayList<>();
        
        OrderDTO order = new OrderDTO();
        order.setOrderId(1L);
        order.setShopId(shopId);
        order.setOrderNo("202401010001");
        order.setTotalAmount(new BigDecimal("99.00"));
        order.setStatus(1);
        order.setCreateTime(LocalDateTime.now());
        list.add(order);
        return list;
    }

    @Override
    public OrderDTO getById(Long shopId, Long orderId) {
        // TODO: 实际从数据库查询
        OrderDTO order = new OrderDTO();
        order.setOrderId(orderId);
        order.setShopId(shopId);
        order.setOrderNo("202401010001");
        order.setTotalAmount(new BigDecimal("99.00"));
        order.setStatus(1);
        order.setCreateTime(LocalDateTime.now());
        return order;
    }
}

package cc.oxshan.api.order.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单 DTO
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long shopId;
    private Long userId;
    private String orderNo;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime createTime;
}

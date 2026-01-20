package cc.oxshan.shop.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户店铺关联实体
 */
@Data
public class UserShop {

    private Long userId;
    private Long shopId;
    private Integer isDefault;
    private LocalDateTime createdAt;
}

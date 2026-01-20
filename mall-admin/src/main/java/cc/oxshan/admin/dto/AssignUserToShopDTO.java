package cc.oxshan.admin.dto;

import lombok.Data;

/**
 * 分配用户到店铺 DTO
 */
@Data
public class AssignUserToShopDTO {
    
    private Long userId;
    private Long shopId;
    private Integer isDefault;
}

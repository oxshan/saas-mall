package cc.oxshan.admin.dto;

import lombok.Data;

/**
 * 移除用户店铺权限 DTO
 */
@Data
public class RemoveUserFromShopDTO {
    
    private Long userId;
    private Long shopId;
}

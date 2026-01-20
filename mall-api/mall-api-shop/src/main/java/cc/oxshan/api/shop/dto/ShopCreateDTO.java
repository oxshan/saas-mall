package cc.oxshan.api.shop.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺创建请求 DTO
 */
@Data
public class ShopCreateDTO implements Serializable {

    private Long parentId;
    private String shopName;
    private Integer shopType;
    private String province;
    private String city;
    private String district;
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String contactName;
    private String contactPhone;
    private String businessHours;
    private String logo;
    private String description;
}

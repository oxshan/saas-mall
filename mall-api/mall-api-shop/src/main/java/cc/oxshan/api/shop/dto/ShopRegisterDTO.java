package cc.oxshan.api.shop.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 店铺注册请求 DTO
 */
@Data
public class ShopRegisterDTO implements Serializable {

    private String shopName;
    private Integer shopType;
    private String province;
    private String city;
    private String district;
    private String address;
    private String contactName;
    private String contactPhone;
    private String businessHours;
    private String logo;
    private String description;
}

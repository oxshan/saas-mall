package cc.oxshan.shop.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 店铺实体
 */
@Data
public class Shop {

    private Long id;
    private Long parentId;
    private String shopCode;
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
    private String images;
    private String description;
    private Integer status;
    private Integer isDeleted;
    private Integer sort;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

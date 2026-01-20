package cc.oxshan.shop.enums;

import lombok.Getter;

/**
 * 店铺状态枚举
 */
@Getter
public enum ShopStatus {

    CLOSED(0, "停业"),
    OPEN(1, "营业");

    private final Integer code;
    private final String desc;

    ShopStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ShopStatus getByCode(Integer code) {
        for (ShopStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}

package cc.oxshan.shop.enums;

import lombok.Getter;

/**
 * 店铺类型枚举
 */
@Getter
public enum ShopType {

    SINGLE_SHOP(1, "单店"),
    CHAIN_HEADQUARTERS(2, "连锁总部"),
    CHAIN_BRANCH(3, "连锁分店");

    private final Integer code;
    private final String desc;

    ShopType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ShopType getByCode(Integer code) {
        for (ShopType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}

package cc.oxshan.common.core.context;

/**
 * 店铺上下文（多租户）
 */
public class ShopContext {

    private static final ThreadLocal<Long> SHOP_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    public static void setShopId(Long shopId) {
        SHOP_ID.set(shopId);
    }

    public static Long getShopId() {
        return SHOP_ID.get();
    }

    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static void clear() {
        SHOP_ID.remove();
        USER_ID.remove();
    }
}

package cc.oxshan.admin.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求头工具类
 * 用于从 Gateway 传递的 Header 中获取用户信息
 */
public class HeaderUtils {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String SHOP_ID_HEADER = "X-Shop-Id";

    private HeaderUtils() {
    }

    /**
     * 获取当前请求
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        return attrs.getRequest();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getUserId(request);
    }

    /**
     * 从请求中获取用户ID
     */
    public static Long getUserId(HttpServletRequest request) {
        String userIdStr = request.getHeader(USER_ID_HEADER);
        if (userIdStr == null || userIdStr.isEmpty()) {
            return null;
        }
        return Long.parseLong(userIdStr);
    }

    /**
     * 获取当前店铺ID
     */
    public static Long getShopId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getShopId(request);
    }

    /**
     * 从请求中获取店铺ID
     */
    public static Long getShopId(HttpServletRequest request) {
        String shopIdStr = request.getHeader(SHOP_ID_HEADER);
        if (shopIdStr == null || shopIdStr.isEmpty()) {
            return null;
        }
        return Long.parseLong(shopIdStr);
    }
}

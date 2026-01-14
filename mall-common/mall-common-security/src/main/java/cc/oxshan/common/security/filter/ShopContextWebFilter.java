package cc.oxshan.common.security.filter;

import cc.oxshan.common.core.constant.CommonConstant;
import cc.oxshan.common.core.context.ShopContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * BFF 层过滤器
 * 从请求头提取 shop_id 和 user_id，放入 ThreadLocal
 */
public class ShopContextWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        try {
            // 从请求头获取 shop_id
            String shopIdStr = request.getHeader(CommonConstant.HEADER_SHOP_ID);
            if (shopIdStr != null && !shopIdStr.isEmpty()) {
                ShopContext.setShopId(Long.parseLong(shopIdStr));
            }

            // 从请求头获取 user_id
            String userIdStr = request.getHeader(CommonConstant.HEADER_USER_ID);
            if (userIdStr != null && !userIdStr.isEmpty()) {
                ShopContext.setUserId(Long.parseLong(userIdStr));
            }

            filterChain.doFilter(request, response);
        } finally {
            // 清理 ThreadLocal，防止内存泄漏
            ShopContext.clear();
        }
    }
}

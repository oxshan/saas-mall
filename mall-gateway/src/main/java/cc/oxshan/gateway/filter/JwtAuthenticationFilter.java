package cc.oxshan.gateway.filter;

import cc.oxshan.gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Gateway JWT 认证过滤器
 * 负责解析 JWT token，提取 userId 和 shopId，并通过请求头传递给下游服务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtils jwtUtils;

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String SHOP_ID_HEADER = "X-Shop-Id";
    
    // 白名单路径，不需要认证
    private static final List<String> WHITE_LIST = List.of(
        "/admin/auth/login",
        "/admin/auth/register",
        "/buyer/auth/login",
        "/buyer/auth/register",
        "/actuator"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 检查是否在白名单中
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取 token
        String token = getTokenFromRequest(exchange.getRequest());
        
        if (!StringUtils.hasText(token)) {
            log.warn("请求路径 {} 缺少 token", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 验证 token
        if (!jwtUtils.validateToken(token)) {
            log.warn("请求路径 {} 的 token 无效", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 解析 token，提取用户信息
        try {
            Long userId = jwtUtils.getUserId(token);
            Long shopId = jwtUtils.getShopId(token);
            
            // 将 userId 和 shopId 添加到请求头，传递给下游服务
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header(USER_ID_HEADER, String.valueOf(userId))
                    .header(SHOP_ID_HEADER, String.valueOf(shopId))
                    .build();
            
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();
            
            log.debug("请求路径 {} 认证成功，userId={}, shopId={}", path, userId, shopId);
            return chain.filter(modifiedExchange);
        } catch (Exception e) {
            log.error("解析 token 失败", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

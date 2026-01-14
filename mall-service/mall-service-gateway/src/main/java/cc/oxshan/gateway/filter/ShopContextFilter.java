package cc.oxshan.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway 全局过滤器
 * 注：ShopId/UserId 通过 Dubbo 接口参数显式传递，不需要 Header 透传
 */
@Component
public class ShopContextFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Gateway 只做路由转发，不处理用户上下文
        // ShopId/UserId 由 BFF 层从 JWT 解析后通过 Dubbo 参数传递
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

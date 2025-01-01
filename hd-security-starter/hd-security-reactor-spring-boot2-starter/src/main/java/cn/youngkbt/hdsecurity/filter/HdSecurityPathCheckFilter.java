package cn.youngkbt.hdsecurity.filter;

import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import cn.youngkbt.hdsecurity.strategy.HdSecurityPathCheckStrategy;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 校验请求 path 是否合法
 *
 * @author Tianke
 * @date 2024/12/30 23:55:55
 * @since 1.0.0
 */
public class HdSecurityPathCheckFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        try {
            HdSecurityPathCheckStrategy.instance.pathCheckFunction.check(request.getPath().toString(), exchange);
        } catch (HdSecurityPathInvalidException e) {
            return HdSecurityPathCheckStrategy.instance.pathInvalidHandleFunction.handle(e, exchange);
        }

        return chain.filter(exchange);
    }
}

package cn.youngkbt.hdsecurity.function;

import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Tianke
 * @date 2024/12/31 00:23:44
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSecurityPathInvalidHandleFunction {

    /**
     * 路径无效时处理函数
     *
     * @param e        路径无效异常
     * @param exchange  Web 上下文
     */
    Mono<Void> handle(HdSecurityPathInvalidException e, ServerWebExchange exchange);
}

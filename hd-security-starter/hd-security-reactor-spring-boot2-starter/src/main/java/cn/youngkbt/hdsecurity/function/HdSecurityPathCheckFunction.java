package cn.youngkbt.hdsecurity.function;

import org.springframework.web.server.ServerWebExchange;

/**
 * @author Tianke
 * @date 2024/12/31 00:15:33
 * @since 1.0.0
 */
@FunctionalInterface
public interface HdSecurityPathCheckFunction {
    /**
     * 路径检查
     * @param path 请求路径
     * @param exchange Web 上下文
     */
    void check(String path, ServerWebExchange exchange);
}

package cn.youngkbt.hdsecurity.utils;

import cn.youngkbt.hdsecurity.constants.WebConstant;
import cn.youngkbt.hdsecurity.model.ContextBox;
import cn.youngkbt.hdsecurity.model.HdSecurityRequestForReactor;
import cn.youngkbt.hdsecurity.model.HdSecurityResponseForReactor;
import cn.youngkbt.hdsecurity.model.HdSecurityStorageForReactor;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Hd Security Reactor 上下文操作（异步），持有当前请求的 ServerWebExchange，因为 WebFlux 没有提供获取 ServerWebExchange 的全局方法
 *
 * @author Tianke
 * @date 2025/1/1 16:57:26
 * @since 1.0.0
 */
public class HdSecurityReactorHolder {

    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    public static final ThreadLocal<ContextBox> CONTEXT = ThreadLocal.withInitial(ContextBox::new);

    /**
     * 存放 WebFilterChain 的 key 
     */
    public static final String CHAIN_KEY = "WEB_FILTER_CHAIN_KEY";

    private HdSecurityReactorHolder() {
    }

    /**
     * 从 Mono 里异步获取上下文
     *
     * @return Web 上下文
     */
    public static Mono<ServerWebExchange> getAsyncContext() {
        return Mono.deferContextual(contextView -> Mono.just(contextView.get(CONTEXT_KEY)));
    }

    /**
     * 设置上下文
     *
     * @param exchange Web 上下文
     */
    public static void setWebExchange(ServerWebExchange exchange) {
        HdSecurityRequestForReactor request = new HdSecurityRequestForReactor(exchange.getRequest());
        HdSecurityResponseForReactor response = new HdSecurityResponseForReactor(exchange.getResponse());
        HdSecurityStorageForReactor storage = new HdSecurityStorageForReactor(exchange);
        ContextBox contextBox = new ContextBox(exchange, request, response, storage);
        CONTEXT.set(contextBox);
    }

    /**
     * 获取上下文
     *
     * @return Web 上下文
     */
    public static ServerWebExchange getWebExchange() {
        ContextBox contextBox = CONTEXT.get();
        if (contextBox == null) {
            return null;
        }
        return contextBox.getExchange();
    }

    /**
     * 获取上下文请求类
     *
     * @return Hd Security 请求类
     */
    public static HdSecurityRequestForReactor getRequest() {
        ContextBox contextBox = CONTEXT.get();
        if (contextBox == null) {
            return null;
        }
        return (HdSecurityRequestForReactor) contextBox.getRequest();
    }

    /**
     * 获取上下文响应类
     *
     * @return Hd Security 响应类
     */
    public static HdSecurityResponseForReactor getResponse() {
        ContextBox contextBox = CONTEXT.get();
        if (contextBox == null) {
            return null;
        }
        return (HdSecurityResponseForReactor) contextBox.getResponse();
    }

    /**
     * 获取上下文会话类
     *
     * @return Hd Security 会话类
     */
    public static HdSecurityStorageForReactor getStorage() {
        ContextBox contextBox = CONTEXT.get();
        if (contextBox == null) {
            return null;
        }
        return (HdSecurityStorageForReactor) contextBox.getStorage();
    }

    /**
     * 清除上下文
     */
    public static void clearContextBox() {
        CONTEXT.remove();
    }

    public static Mono<Void> responseWrite(ServerHttpResponse response, Object message) {
        return responseWrite(response, "text/plain; charset=utf-8", message);
    }

    public static Mono<Void> responseWrite(ServerHttpResponse response, String contentType, Object message) {
        if (HdStringUtil.hasEmpty(response.getHeaders().getFirst(WebConstant.CONTENT_TYPE))) {
            response.getHeaders().set(WebConstant.CONTENT_TYPE, contentType);
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(String.valueOf(message).getBytes())));
    }
}

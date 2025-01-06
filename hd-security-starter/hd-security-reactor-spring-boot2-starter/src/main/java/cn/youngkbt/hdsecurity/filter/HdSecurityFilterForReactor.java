package cn.youngkbt.hdsecurity.filter;

import cn.youngkbt.hdsecurity.error.HdSecuritySpringReactorErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityBreakMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.router.HdRouter;
import cn.youngkbt.hdsecurity.utils.HdSecurityReactorHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Hd Security 鉴权过滤器
 *
 * @author Tianke
 * @date 2025/1/1 17:47:19
 * @since 1.0.0
 */
public class HdSecurityFilterForReactor implements WebFilter, HdSecurityFilter {
    /**
     * 拦截路由
     */
    private List<String> includeList = new ArrayList<>();

    /**
     * 放行路由
     */
    private List<String> excludeList = new ArrayList<>();

    /**
     * 认证前执行函数
     */
    private Runnable beforeAuth = () -> {
    };

    /**
     * 认证函数
     */
    private Runnable auth = () -> {
    };

    /**
     * 认证异常或者逻辑异常处理函数
     */
    private Function<Throwable, Object> error = e -> {
        throw new HdSecurityException(e).setCode(HdSecuritySpringReactorErrorCode.DEFAULT_FILTER_ERROR);
    };

    @Override
    public HdSecurityFilterForReactor addInclude(String... paths) {
        includeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityFilterForReactor addExclude(String... paths) {
        excludeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityFilterForReactor setIncludeList(List<String> pathList) {
        includeList = pathList;
        return this;
    }

    @Override
    public HdSecurityFilterForReactor setExcludeList(List<String> pathList) {
        excludeList = pathList;
        return this;
    }

    @Override
    public HdSecurityFilter setBeforeAuth(Runnable beforeAuth) {
        this.beforeAuth = beforeAuth;
        return this;
    }

    @Override
    public HdSecurityFilter setAuth(Runnable auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public HdSecurityFilter setError(Function<Throwable, Object> error) {
        this.error = error;
        return this;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 在作用域里写入 WebFilterChain 对象，HdSecurityRequestForReactor 的 forward 方法取出来用到
        exchange.getAttributes().put(HdSecurityReactorHolder.CHAIN_KEY, chain);

        // 写入全局上下文（同步） 
        HdSecurityReactorHolder.setWebExchange(exchange);

        try {
            beforeAuth.run();
            HdRouter.match(includeList).notMatch(excludeList).check(() -> auth.run());
        } catch (HdSecurityStopException e) {
            // HdSecurityStopException 异常代表：停止匹配，进入 Controller
        } catch (Exception e) {
            Object message;
            if (e instanceof HdSecurityBreakMatchException) {
                message = e.getMessage();
            } else {
                message = error.apply(e);
            }
            return HdSecurityReactorHolder.responseWrite(exchange.getResponse(), message);
        } finally {
            HdSecurityReactorHolder.clearContextBox();
        }

        return chain.filter(exchange).contextWrite(contextView -> {
                    // 写入全局上下文（异步）
                    contextView.put(HdSecurityReactorHolder.CONTEXT_KEY, exchange);
                    return contextView;
                })
                // 清除上下文
                .doFinally(signalType -> HdSecurityReactorHolder.clearContextBox());
    }
}

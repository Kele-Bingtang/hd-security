package cn.youngkbt.hdsecurity.filter;

import cn.youngkbt.hdsecurity.error.HdSecuritySpringErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityBreakMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.router.HdRouter;
import cn.youngkbt.hdsecurity.utils.SpringMVCHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Hd Security 鉴权过滤器
 *
 * @author Tianke
 * @date 2024/12/31 00:34:37
 * @since 1.0.0
 */
public class HdSecurityFilterForServlet implements Filter, HdSecurityFilter {

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
        throw new HdSecurityException(e).setCode(HdSecuritySpringErrorCode.DEFAULT_FILTER_ERROR);
    };

    @Override
    public HdSecurityFilterForServlet addInclude(String... paths) {
        includeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityFilterForServlet addExclude(String... paths) {
        excludeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityFilterForServlet setIncludeList(List<String> pathList) {
        includeList = pathList;
        return this;
    }

    @Override
    public HdSecurityFilterForServlet setExcludeList(List<String> pathList) {
        excludeList = pathList;
        return this;
    }

    @Override
    public HdSecurityFilterForServlet setBeforeAuth(Runnable beforeAuth) {
        this.beforeAuth = beforeAuth;
        return this;
    }

    @Override
    public HdSecurityFilterForServlet setAuth(Runnable auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public HdSecurityFilterForServlet setError(Function<Throwable, Object> error) {
        this.error = error;
        return this;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        beforeAuth.run();
        try {
            HdRouter.match(includeList).notMatch(excludeList).check(() -> auth.run());
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (HdSecurityStopException e) {
            // HdSecurityStopException 异常代表：停止匹配，进入 Controller
        } catch (Exception e) {
            Object message;
            if (e instanceof HdSecurityBreakMatchException) {
                message = e.getMessage();
            } else {
                message = error.apply(e);
            }
            SpringMVCHolder.responseWrite((HttpServletResponse) servletResponse, message);

        }
    }
}

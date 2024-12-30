package cn.youngkbt.hdsecurity.filter;

import cn.youngkbt.hdsecurity.error.HdSecuritySpringErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityContinueMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.router.HdRouter;
import cn.youngkbt.hdsecurity.utils.SpringMVCUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Tianke
 * @date 2024/12/31 00:34:37
 * @since 1.0.0
 */
public class HdSecurityServletFilter implements Filter, HdSecurityFilter {

    /**
     * 拦截路由
     */
    public List<String> includeList = new ArrayList<>();

    /**
     * 放行路由
     */
    public List<String> excludeList = new ArrayList<>();

    public Runnable beforeAuth = () -> {
    };

    public Runnable auth = () -> {
    };

    public Function<Throwable, Object> error = e -> {
        throw new HdSecurityException(e).setCode(HdSecuritySpringErrorCode.DEFAULT_FILTER_ERROR);
    };

    @Override
    public HdSecurityServletFilter addInclude(String... paths) {
        includeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityServletFilter addExclude(String... paths) {
        excludeList.addAll(Arrays.asList(paths));
        return this;
    }

    @Override
    public HdSecurityServletFilter setIncludeList(List<String> pathList) {
        includeList = pathList;
        return this;
    }

    @Override
    public HdSecurityServletFilter setExcludeList(List<String> pathList) {
        excludeList = pathList;
        return this;
    }

    @Override
    public HdSecurityServletFilter setBeforeAuth(Runnable beforeAuth) {
        this.beforeAuth = beforeAuth;
        return this;
    }

    @Override
    public HdSecurityServletFilter setAuth(Runnable auth) {
        this.auth = auth;
        return this;
    }

    @Override
    public HdSecurityServletFilter setError(Function<Throwable, Object> error) {
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
        } catch (Throwable e) {
            Object message;
            if (e instanceof HdSecurityContinueMatchException) {
                message = e.getMessage();
            } else {
                message = error.apply(e);
            }
            SpringMVCUtil.responseWrite((HttpServletResponse) servletResponse, message);
        }
    }
}

package cn.youngkbt.hdsecurity.filter;

import cn.youngkbt.hdsecurity.exception.HdSecurityPathInvalidException;
import cn.youngkbt.hdsecurity.strategy.HdSecurityPathCheckStrategy;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 校验请求 path 是否合法
 *
 * @author Tianke
 * @date 2024/12/31 00:33:17
 * @since 1.0.0
 */
public class HdSecurityPathCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest request && servletResponse instanceof HttpServletResponse response) {
            try {
                HdSecurityPathCheckStrategy.instance.pathCheckFunction.check(request.getRequestURI(), request, response);
            } catch (HdSecurityPathInvalidException e) {
                HdSecurityPathCheckStrategy.instance.pathInvalidHandleFunction.handle(e, request, response);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
package cn.youngkbt.hdsecurity.interceptor;

import cn.youngkbt.hdsecurity.exception.HdSecurityContinueMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Consumer;

/**
 * 权限路由拦截器，进行路由拦截鉴权
 *
 * @author Tianke
 * @date 2024/12/24 00:33:23
 * @since 1.0.0
 */
public class HdSecurityFunctionInterceptor implements HandlerInterceptor {

    public Consumer<Object> auth = handler -> {
    };

    public void setAuth(Consumer<Object> auth) {
        this.auth = auth;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            auth.accept(handler);
        } catch (HdSecurityStopException e) {
            // HdSecurityStopException 异常代表：停止匹配，进入 Controller

        } catch (HdSecurityContinueMatchException e) {
            if (response.getContentType() == null) {
                response.setContentType("text/plain; charset=utf-8");
            }
            response.getWriter().print(e.getMessage());
            return false;
        }
        return true;

    }
}

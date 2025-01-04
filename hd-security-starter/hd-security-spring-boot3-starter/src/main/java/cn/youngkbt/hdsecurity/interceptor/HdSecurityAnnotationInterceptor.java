package cn.youngkbt.hdsecurity.interceptor;

import cn.youngkbt.hdsecurity.exception.HdSecurityContinueMatchException;
import cn.youngkbt.hdsecurity.exception.HdSecurityStopException;
import cn.youngkbt.hdsecurity.hd.HdAnnotationHelper;
import cn.youngkbt.hdsecurity.hd.HdHelper;
import cn.youngkbt.hdsecurity.utils.SpringMVCHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Hd Security 权限注解拦截器，进行注解鉴权
 *
 * @author Tianke
 * @date 2024/12/24 00:27:06
 * @since 1.0.0
 */
public class HdSecurityAnnotationInterceptor implements HandlerInterceptor {

    /**
     * 每次请求之前尝试校验路由规则
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // handler 是 HandlerMethod 类型时，才能进行注解鉴权
            if (!(handler instanceof HandlerMethod)) {
                return false;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            HdAnnotationHelper hdAnnotationHelper = HdHelper.annotationHelper();
            hdAnnotationHelper.handle.accept(handlerMethod.getMethod());
        } catch (HdSecurityStopException e) {
            // HdSecurityStopException 异常代表：停止匹配，进入 Controller

        } catch (HdSecurityContinueMatchException e) {
            SpringMVCHolder.responseWrite(response, e.getMessage());
            return false;
        }
        return true;
    }
}

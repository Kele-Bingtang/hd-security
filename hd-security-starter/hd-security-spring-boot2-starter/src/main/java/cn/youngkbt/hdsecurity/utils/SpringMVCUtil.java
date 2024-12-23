package cn.youngkbt.hdsecurity.utils;

import cn.youngkbt.hdsecurity.error.HdSecuritySpringErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityNotWebContextException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tianke
 * @date 2024/12/24 00:36:55
 * @since 1.0.0
 */
public class SpringMVCUtil {

    private SpringMVCUtil() {
    }

    /**
     * 获取当前会话的 request 对象
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new HdSecurityNotWebContextException("非 web 上下文无法获取 HttpServletRequest").setCode(HdSecuritySpringErrorCode.NOT_WEB_CONTEXT);
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取当前会话的 response 对象
     *
     * @return response
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new HdSecurityNotWebContextException("非 web 上下文无法获取 HttpServletResponse").setCode(HdSecuritySpringErrorCode.NOT_WEB_CONTEXT);
        }
        return servletRequestAttributes.getResponse();
    }

    /**
     * 判断当前是否处于 Web 上下文中
     *
     * @return 当前是否处于 Web 上下文中
     */
    public static boolean isWeb() {
        return null != RequestContextHolder.getRequestAttributes();
    }
}

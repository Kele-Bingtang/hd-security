package cn.youngkbt.hdsecurity.utils;

import cn.youngkbt.hdsecurity.error.HdSecuritySpringErrorCode;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import cn.youngkbt.hdsecurity.exception.HdSecurityNotWebContextException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;

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

    public static void responseWrite(HttpServletResponse response, Object message) {
        responseWrite(response, "text/plain; charset=utf-8", message);
    }

    public static void responseWrite(HttpServletResponse response, String contentType, Object message) {
        response.setContentType(contentType);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new HdSecurityException(e);
        }
        writer.print(message);
        writer.flush();
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

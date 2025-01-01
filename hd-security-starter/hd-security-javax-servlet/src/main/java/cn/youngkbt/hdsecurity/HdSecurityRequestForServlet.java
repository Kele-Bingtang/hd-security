package cn.youngkbt.hdsecurity;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCodeForServlet;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HdSecurityRequest 的实现
 *
 * @author Tianke
 * @date 2024/12/24 00:50:42
 * @since 1.0.0
 */
public class HdSecurityRequestForServlet implements HdSecurityRequest {
    /**
     * Request 对象
     */
    protected HttpServletRequest request;

    public HdSecurityRequestForServlet(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object getSource() {
        return request;
    }

    @Override
    public String getParam(String name) {
        return request.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParamsMap() {
        return request.getParameterMap();
    }

    @Override
    public Map<String, String> getParamMap() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> map = new LinkedHashMap<>(parameterMap.size());

        parameterMap.forEach((key, value) -> map.put(key, value[0]));

        return map;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * 在 Cookie 作用域里获取一个值（key 重复则取最后一个值）
     *
     * @param name 键
     * @return 值
     */
    @Override
    public String getCookieValue(String name) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (null != cookie && Objects.equals(cookie.getName(), name)) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }

    @Override
    public String getRequestPath() {
        return request.getRequestURI();
    }

    @Override
    public String getUrl() {
        return request.getContextPath();
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public Object forward(String path) {
        HttpServletResponse response = (HttpServletResponse) HdSecurityManager.getContext().getResponse().getSource();
        try {
            request.getRequestDispatcher(path).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new HdSecurityException(e).setCode(HdSecurityErrorCodeForServlet.FORWARD_FAILURE);
        }
        return null;
    }
}

package cn.youngkbt.hdsecurity;

import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import jakarta.servlet.http.HttpServletRequest;


/**
 * HdSecurityStorage 的实现
 *
 * @author Tianke
 * @date 2024/12/24 01:04:52
 * @since 1.0.0
 */
public class HdSecurityStorageForServlet implements HdSecurityStorage {
    /**
     * Request 对象
     */
    protected HttpServletRequest request;

    public HdSecurityStorageForServlet(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object getSource() {
        return request;
    }

    @Override
    public Object get(String key) {
        return request.getAttribute(key);
    }

    @Override
    public HdSecurityStorage set(String key, Object value) {
        request.setAttribute(key, value);
        return this;
    }

    @Override
    public HdSecurityStorage remove(String key) {
        request.removeAttribute(key);
        return this;
    }
}

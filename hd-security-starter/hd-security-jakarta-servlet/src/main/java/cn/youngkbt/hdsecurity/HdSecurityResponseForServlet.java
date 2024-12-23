package cn.youngkbt.hdsecurity;

import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.error.HdSecurityErrorCodeForServlet;
import cn.youngkbt.hdsecurity.exception.HdSecurityException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * HdSecurityResponse 的实现
 *
 * @author Tianke
 * @date 2024/12/24 01:00:55
 * @since 1.0.0
 */
public class HdSecurityResponseForServlet implements HdSecurityResponse {

    /**
     * Response 对象
     */
    protected HttpServletResponse response;

    public HdSecurityResponseForServlet(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public Object getSource() {
        return response;
    }

    @Override
    public HdSecurityResponse addHeader(String name, String value) {
        response.setHeader(name, value);
        return this;
    }

    @Override
    public Object redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new HdSecurityException(e).setCode(HdSecurityErrorCodeForServlet.REDIRECT_FAILURE);
        }
        return null;
    }

    @Override
    public HdSecurityResponse setStatus(int code) {
        response.setStatus(code);
        return this;
    }
}

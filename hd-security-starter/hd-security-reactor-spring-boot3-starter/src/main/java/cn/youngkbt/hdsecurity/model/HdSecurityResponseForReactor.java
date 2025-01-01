package cn.youngkbt.hdsecurity.model;

import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import java.net.URI;

/**
 * HdSecurityResponse 的实现
 *
 * @author Tianke
 * @date 2025/1/1 16:12:12
 * @since 1.0.0
 */
public class HdSecurityResponseForReactor implements HdSecurityResponse {

    private ServerHttpResponse response;

    public HdSecurityResponseForReactor(ServerHttpResponse response) {
        this.response = response;
    }

    @Override
    public Object getSource() {
        return response;
    }

    @Override
    public HdSecurityResponse addHeader(String name, String value) {
        response.getHeaders().add(name, value);
        return this;
    }

    @Override
    public Object redirect(String url) {
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(url));
        
        return null;
    }

    @Override
    public HdSecurityResponse setStatus(int code) {
        response.setStatusCode(HttpStatus.valueOf(code));
        return this;
    }
}

package cn.youngkbt.hdsecurity.model;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Tianke
 * @date 2025/1/1 17:10:57
 * @since 1.0.0
 */
public class ContextBox {
    private ServerWebExchange exchange;
    private HdSecurityRequest request;
    private HdSecurityResponse response;
    private HdSecurityStorage storage;

    public ContextBox() {
    }

    public ContextBox(ServerWebExchange exchange, HdSecurityRequest request, HdSecurityResponse response, HdSecurityStorage storage) {
        this.exchange = exchange;
        this.request = request;
        this.response = response;
        this.storage = storage;
    }

    public ServerWebExchange getExchange() {
        return exchange;
    }

    public ContextBox setExchange(ServerWebExchange exchange) {
        this.exchange = exchange;
        return this;
    }

    public HdSecurityRequest getRequest() {
        return request;
    }

    public ContextBox setRequest(HdSecurityRequest request) {
        this.request = request;
        return this;
    }

    public HdSecurityResponse getResponse() {
        return response;
    }

    public ContextBox setResponse(HdSecurityResponse response) {
        this.response = response;
        return this;
    }

    public HdSecurityStorage getStorage() {
        return storage;
    }

    public ContextBox setStorage(HdSecurityStorage storage) {
        this.storage = storage;
        return this;
    }
}

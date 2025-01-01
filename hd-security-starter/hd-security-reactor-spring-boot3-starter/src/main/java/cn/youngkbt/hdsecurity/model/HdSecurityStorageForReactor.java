package cn.youngkbt.hdsecurity.model;

import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;
import org.springframework.web.server.ServerWebExchange;

/**
 * HdSecurityStorage 的实现
 *
 * @author Tianke
 * @date 2025/1/1 16:12:20
 * @since 1.0.0
 */
public class HdSecurityStorageForReactor implements HdSecurityStorage {

    /**
     * 底层 ServerWebExchange 对象
     */
    protected ServerWebExchange exchange;

    public HdSecurityStorageForReactor(ServerWebExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public Object getSource() {
        return exchange;
    }

    @Override
    public Object get(String key) {
        return exchange.getAttribute(key);
    }

    @Override
    public HdSecurityStorage set(String key, Object value) {
        exchange.getAttributes().put(key, value);
        return this;
    }

    @Override
    public HdSecurityStorage remove(String key) {
        exchange.getAttributes().remove(key);
        return this;
    }
}

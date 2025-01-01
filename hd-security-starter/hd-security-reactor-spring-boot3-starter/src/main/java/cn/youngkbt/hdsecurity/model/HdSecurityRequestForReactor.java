package cn.youngkbt.hdsecurity.model;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.utils.HdSecurityReactorHolder;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HdSecurityRequest 的实现
 *
 * @author Tianke
 * @date 2025/1/1 16:11:35
 * @since 1.0.0
 */
public class HdSecurityRequestForReactor implements HdSecurityRequest {

    /**
     * Request 对象
     */
    private ServerHttpRequest request;

    public HdSecurityRequestForReactor(ServerHttpRequest request) {
        this.request = request;
    }

    @Override
    public Object getSource() {
        return request;
    }

    @Override
    public String getParam(String name) {
        return request.getQueryParams().getFirst(name);
    }

    @Override
    public Map<String, String[]> getParamsMap() {
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        Map<String, String[]> map = new LinkedHashMap<>(queryParams.size());

        queryParams.forEach((key, value) -> map.put(key, value.toArray(new String[0])));
        return map;
    }

    @Override
    public Map<String, String> getParamMap() {
        return request.getQueryParams().toSingleValueMap();
    }

    @Override
    public String getHeader(String name) {
        return request.getHeaders().getFirst(name);
    }

    /**
     * 在 Cookie 作用域里获取一个值（key 重复则取最后一个值）
     *
     * @param name 键
     * @return 值
     */
    @Override
    public String getCookieValue(String name) {
        HttpCookie cookie = request.getCookies().getFirst(name);
        return cookie != null ? cookie.getValue() : null;
    }

    @Override
    public String getRequestPath() {
        return request.getPath().value();
    }

    @Override
    public String getUrl() {
        return request.getURI().toString();
    }

    @Override
    public String getMethod() {
        HttpMethod method = request.getMethod();
        return null != method ? method.name() : null;
    }

    @Override
    public Object forward(String path) {
        ServerWebExchange exchange = HdSecurityReactorHolder.getWebExchange();
        WebFilterChain chain = exchange.getAttribute(HdSecurityReactorHolder.CHAIN_KEY);

        ServerHttpRequest newRequest = request.mutate().path(path).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }
}

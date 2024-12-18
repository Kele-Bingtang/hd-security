package cn.youngkbt.hdsecurity.context;

import cn.youngkbt.hdsecurity.context.model.HdSecurityRequest;
import cn.youngkbt.hdsecurity.context.model.HdSecurityResponse;
import cn.youngkbt.hdsecurity.context.model.HdSecurityStorage;

/**
 * @author Tianke
 * @date 2024/11/30 15:25:44
 * @since 1.0.0
 */
public interface HdSecurityContext {
    /**
     * 获取当前请求的 Request 包装对象
     * @return Request 包装对象
     */
    HdSecurityRequest getRequest();

    /**
     * 获取当前请求的 Response 包装对象
     * @return Response 包装对象
     */
    HdSecurityResponse getResponse();

    /**
     * 获取当前请求的 Storage 包装对象
     * @return Storage 包装对象
     */
    HdSecurityStorage getStorage();

    /**
     * 判断：指定路由匹配符是否可以匹配成功指定路径
     * <pre>
     *     判断规则由底层 web 框架决定，例如在 springboot 中：
     *     	- matchPath("/user/*", "/user/login")  返回: true
     *     	- matchPath("/user/*", "/article/edit")  返回: false
     * </pre>
     *
     * @param pattern 路由匹配符 
     * @param path 需要匹配的路径 
     * @return 是否匹配成功
     */
    boolean matchPath(String pattern, String path);
}
